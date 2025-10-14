drop database if exists SimpleBistro;

CREATE DATABASE SimpleBistro;

USE SimpleBistro;

CREATE TABLE Tax (
    TaxID int primary key auto_increment,
    TaxPercentage decimal(5,2) not null,
	StartDate date not null,
	EndDate date null
);

CREATE TABLE PaymentType (
    PaymentTypeID int primary key auto_increment,
    PaymentTypeName varchar(25) not null
);

CREATE TABLE ItemCategory (
    ItemCategoryID int primary key auto_increment,
    ItemCategoryName varchar(25) not null
);

CREATE TABLE Item (
    ItemID int primary key auto_increment,
    ItemCategoryID int not null,
    ItemName varchar(25) not null,
    ItemDescription varchar(255) not null,
	StartDate date not null,
	EndDate date null,
	UnitPrice decimal(7,2) not null default 0,
	constraint fk_item_ItemCategory_id
        foreign key (ItemCategoryID)
        references ItemCategory(ItemCategoryID)
);

CREATE TABLE Server (
    ServerID int primary key auto_increment,
    FirstName varchar(25) not null,
	LastName varchar(25) not null,
	HireDate date not null,
	TermDate date null
);

CREATE TABLE `Order` (
    OrderID int primary key auto_increment,
    ServerID int not null,
	OrderDate datetime not null,
	SubTotal decimal(9,2) not null default 0,
	Tax decimal(9,2) not null default 0,
	Tip decimal(9,2) not null default 0,
	Total decimal (9,2) not null default 0,
	constraint fk_item_OrderServer_id
        foreign key (ServerID)
        references Server(ServerID)
);

CREATE TABLE OrderItem (
    OrderItemID int primary key auto_increment,
    OrderID int not null,
    ItemID int not null,
    Quantity int not null default 1,
    Price decimal(9,2) not null	default 0,
    constraint fk_OrderItem_OrderID_id
        foreign key (OrderID)
        references `Order`(OrderID),
	constraint fk_OrderItem_ItemID_id
        foreign key (ItemID)
        references Item(ItemID)
);

CREATE TABLE Payment (
    PaymentID int primary key auto_increment,
    PaymentTypeID int not null,   
    OrderID int not null,
    Amount decimal(9,2) not null default 0,
	constraint fk_Payment_PaymentTypeID_id
        foreign key (PaymentTypeID)
        references PaymentType(PaymentTypeID),
    constraint fk_Payment_OrderID_id
        foreign key (OrderID)
        references `Order`(OrderID)
);