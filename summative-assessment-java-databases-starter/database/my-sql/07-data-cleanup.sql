-- fix random term date before hire date
Update `Server`
	SET HireDate = '2020-01-01'
WHERE HireDate > TermDate;

-- fix items on orders before item was available, they all get chicken sandwiches
Update OrderItem, (
SELECT oi.OrderItemId
FROM `Order` o 
	INNER JOIN OrderItem oi ON o.OrderId = oi.OrderId
	INNER JOIN Item i ON oi.ItemID = i.ItemID
WHERE o.OrderDate < i.startdate
) as fix
SET ItemId = 19
WHERE OrderItem.OrderItemId= fix.OrderItemId ;

-- fix seasonal items not in order date range, they all get caeser salads
UPDATE OrderItem, 
	(
	SELECT oi.OrderItemId
	FROM `Order` o 
		INNER JOIN OrderItem oi ON o.OrderId = oi.OrderId
		INNER JOIN Item i ON oi.ItemID = i.ItemID
	WHERE NOT o.OrderDate BETWEEN i.startdate AND ifnull(i.enddate, '9999-12-31'))
    as OutOfRange
SET ItemID = 16
WHERE OrderItem.OrderItemId = OutOfRange.OrderItemId ;

-- Now set the Price on the OrderItem appropriate for the quantity
Update OrderItem oi1, (SELECT oi.OrderItemID, ExtendedPrice from (SELECT oi.OrderItemID, Quantity * UnitPrice AS ExtendedPrice
 	FROM OrderItem oi 
 		INNER JOIN Item i ON oi.ItemId = i.ItemID) AS q INNER JOIN OrderItem oi ON q.OrderItemID = oi.OrderItemID) as q2
        set oi1.Price = q2.ExtendedPrice
        where oi1.OrderItemId = q2.OrderItemID;

-- Update subtotal on orders to sum from order item
UPDATE `Order` o1, 
       (SELECT SUM(Price) as Total, OrderID
	      FROM OrderItem
	      Group By OrderID) as oi 
		SET o1.SubTotal = oi.Total
        where o1.OrderId = oi.OrderID;
	
-- Remove orders with no items (only 35)
DELETE od FROM `Order` AS od WHERE od.OrderID IN 
(SELECT OrderID 
	FROM (
	SELECT DISTINCT(o.OrderID) 
	FROM `Order` as o
		LEFT JOIN OrderItem oi ON o.OrderID = oi.OrderID
	WHERE oi.OrderID IS NULL)
x);

-- Handle Tax & Tip
UPDATE `Order` SET 
	Tax = CASE WHEN OrderDate < '2022-01-01' THEN SubTotal * 0.0575 ELSE SubTotal * 0.0625 END,
	Tip = CASE WHEN Subtotal < 20 THEN SubTotal * 0.10 WHEN SubTotal < 100 THEN SubTotal * 0.15 ELSE Subtotal * 0.20 END;

-- Handle final order total
UPDATE `Order` SET 
	Total = SubTotal + Tax + Tip;

