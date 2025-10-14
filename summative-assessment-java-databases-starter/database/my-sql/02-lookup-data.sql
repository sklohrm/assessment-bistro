USE SimpleBistro;

INSERT INTO Tax (TaxPercentage, StartDate, EndDate)
VALUES (5.75, '2020-01-01', '2021-12-31'),
(6.25, '2022-01-01', null);

INSERT INTO PaymentType (PaymentTypeName)
VALUES ('Cash'),
('Visa'),
('Mastercard'),
('Paypal'),
('Bitcoin');

INSERT INTO ItemCategory (ItemCategoryName)
VALUES ('Appetizers'),
('Beverages'),
('Soups'),
('Salads'),
('Lunch Entrees'),
('Breakfast Entrees'),
('Desserts')