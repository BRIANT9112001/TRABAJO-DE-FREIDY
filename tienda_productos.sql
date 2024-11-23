DROP DATABASE tienda_productos;

CREATE DATABASE tienda_productos;
USE tienda_productos;


DROP TABLE IF EXISTS productos;

CREATE TABLE productos (
  id int NOT NULL AUTO_INCREMENT,       
  nombre varchar(255) NOT NULL,        
  precio decimal(10,2) NOT NULL,       
  descripcion text,                    
  imagen_ruta varchar(255) DEFAULT NULL, 
  PRIMARY KEY (id),
  UNIQUE (nombre),
  UNIQUE(precio)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


DROP TABLE IF EXISTS clientes;
CREATE TABLE clientes (
  id int NOT NULL AUTO_INCREMENT,
  nombre varchar(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS Factura;
CREATE TABLE Factura (
  id int NOT NULL AUTO_INCREMENT,
  fecha datetime NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE Factura_Productos (
    id INT NOT NULL AUTO_INCREMENT,
    factura_id INT NOT NULL,
    productos_nombre VARCHAR(255) NOT NULL,
    productos_precio DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (factura_id) REFERENCES Factura(id),
    FOREIGN KEY (productos_nombre) REFERENCES productos(nombre),
    FOREIGN KEY (productos_precio) REFERENCES productos(precio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS Empleados;
CREATE TABLE Empleados ( 
  id int NOT NULL AUTO_INCREMENT,
  Nombre VARCHAR(45) NOT NULL,
  Posicion VARCHAR(45) NOT NULL,
  Salario VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

DROP TABLE IF EXISTS ContactosEmpleados;
CREATE TABLE ContactosEmpleados (
id int NOT NULL auto_increment,
email VARCHAR(255) NOT NULL,
Empleados_id int NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (Empleados_id) REFERENCES Empleados(id)
);

DROP TABLE IF EXISTS ContactosClientes;
CREATE TABLE ContactosClientes (
id int NOT NULL auto_increment,
tipocontacto VARCHAR(30) NOT NULL,
detallecontacto VARCHAR(60) NOT NULL,
clientes_id int NOT NULL,
PRIMARY KEY (id),
FOREIGN KEY (clientes_id) REFERENCES clientes(id)
);
