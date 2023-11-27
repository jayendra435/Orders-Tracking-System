create database OrderTrackingDB

-- Create the Customers table
CREATE TABLE Customers (
    customer_id INT IDENTITY(1,1) ,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(40) NOT NULL,
    mobile VARCHAR(10) NOT NULL,
    CHECK (mobile LIKE '[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]'),
    CONSTRAINT PK_customer PRIMARY KEY(customer_id)
);

-- Create the Products table
CREATE TABLE Products (
    product_id INT IDENTITY(100,1) ,
    name VARCHAR(50) NOT NULL,
    description TEXT,
    price MONEY NOT NULL,
     CONSTRAINT PK_products PRIMARY KEY(product_id)
);

-- Create the Order table

CREATE TABLE Orders (
    order_id INT IDENTITY(200,1),
    orderDate DATE NOT NULL,
    customer_id INT, 
    status VARCHAR(10) NOT NULL,
    deliveryDate DATE,
    CONSTRAINT PK_orders PRIMARY KEY(order_id),
    CONSTRAINT FK_customer_order
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

-- Create the OrderItems table
CREATE TABLE OrderItems (
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price MONEY NOT NULL,
    CONSTRAINT PK_order_items PRIMARY KEY (order_id, product_id),
    CONSTRAINT FK_order_items
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    CONSTRAINT FK_product_order
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);




select * from Customers;
select * from products; 
select * from orders ;
select * from OrderItems;



INSERT INTO Customers (name, email, mobile)
VALUES 
    ('Jayendra Chowdary', 'jayendra.chowdary@gmail.com', '9876543210'),
    ('Satish Jakkuva', 'satish.jakkuva@gmail.com', '1234567890'),
    ('Chakitha', 'Chakitha@gmail.com', '9823480323');


INSERT INTO Products (name, description, price)
VALUES 
    ('Saree', 'Beautiful traditional attire', 1500.00),
    ('Kurta', 'Comfortable and stylish casual wear', 800.00),
    ('Trousers', 'Formal trousers for a professional look', 1200.00),
    ('Smartphone', 'High-end smartphone with advanced features', 20000.00),
    ('Laptop', 'Powerful laptop for work and entertainment', 50000.00),
    ('Headphones', 'High-quality headphones for immersive audio', 1500.00);


INSERT INTO Orders (orderDate, customer_id, status, deliveryDate)
VALUES 
    ('2023-10-01', 1, 'n', '2023-11-15'),
    ('2023-10-02', 2, 'd', '2023-11-10'),
    ('2023-10-03', 3, 'c', NULL);


INSERT INTO OrderItems (order_id, product_id, quantity, price)
VALUES 
    (202, 100, 2, 100.00),
    (202, 103, 1, 200.00),
    (203, 101, 1, 75.00),
    (203, 104, 2, 1000.00),
    (204, 102, 3, 300.00),
    (204, 105, 1, 150.00);



select * from Customers;
select * from products; 
select * from orders ;
select * from OrderItems;
