CREATE TABLE car_info (
    vin CHAR(17) NOT NULL,
    corp VARCHAR(25),
    model VARCHAR(25),
    model_year CHAR(4),
    owner VARCHAR(30),
    gender CHAR(1),      -- M/F
    PRIMARY KEY (vin)
);