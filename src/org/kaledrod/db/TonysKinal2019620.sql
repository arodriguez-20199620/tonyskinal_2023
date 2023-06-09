/** 
	Angel Kaled Rodriguez Soc
	Carnet: 2019620
	Código técnico: IN5BM
	fecha de creación: 
		28/03/2023
	fechas de modificaciones: 
		31-01-2023
**/

Drop database if exists DBTonysKinal2019620; 
Create database DBTonysKinal2019620;
 
Use DBTonysKinal2019620;

Create table Empresas(
	codigoEmpresa int auto_increment not null,
    nombreEmpresa varchar(150) not null, 
    direccion varchar(150) not null, 
    telefono varchar(8),
	primary key PK_codigoEmpresa (codigoEmpresa)
); 

Create table TipoEmpleado( 
	codigoTipoEmpleado int not null auto_increment,
    descripcion varchar(50) not null, 
    primary key PK_codigoTipoEmpleado (codigoTipoEmpleado)
);

Create table TipoPlato(
	codigoTipoPlato int not null auto_increment, 
    descripcionTipo varchar(100) not null, 
    primary key PK_codigoTipoPlato (codigoTipoPlato) 
);

Create table Productos(
	codigoProducto int not null auto_increment,
    nombreProducto varchar(150) not null, 
    cantidad int not null, 
    primary key PK_codigoProducto (codigoProducto)
); 

Create table Empleados(
	codigoEmpleado int not null auto_increment, 
    numeroEmpleado int not null, 
    apellidosEmpleado varchar(150) not null, 
    nombresEmpleado varchar(150) not null,
    direccionEmpleado varchar(150) not null,
    telefonoContacto varchar(8) not null,
    gradoCocinero varchar(50), 
    codigoTipoEmpleado int not null, 
    primary key PK_codigoEmpleado (codigoEmpleado),
	constraint FK_Empleados_TipoEmpleado foreign key
		(codigoTipoEmpleado) references TipoEmpleado(codigoTipoEmpleado) on delete cascade
);

Create table Servicios(
	codigoServicio int not null auto_increment, 
    fechaServicio date not null, 
    tipoServicio varchar (150) not null, 
    horaServicio time not null, 
    lugarServicio varchar (150) not null, 
    telefonoContacto varchar (150) not null, 
    codigoEmpresa int not null, 
    primary key PK_codigoServicio (codigoServicio), 
    constraint FK_Servicios_Empresas foreign key (codigoEmpresa) 
		references Empresas(codigoEmpresa) on delete cascade
); 

Create table Presupuestos(
	codigoPresupuesto int not null auto_increment, 
    fechaSolicitud date not null, 
    cantidadPresupuesto decimal(10, 2) not null, 
    codigoEmpresa int not null, 
    primary key PK_codigoPresupuesto (codigoPresupuesto), 
    constraint FK_Presupuesto_Empresas foreign key (codigoEmpresa)
		references Empresas(codigoEmpresa) on delete cascade
); 

Create table Platos(
	codigoPlato int not null auto_increment, 
    cantidad int not null, 
    nombrePlato varchar(150) not null, 
    descripcion varchar(150) not null, 
    precioPlato decimal (10, 2) not null, 
    codigoTipoPlato int not null,
    primary key PK_codigoPlato (codigoPlato), 
    constraint FK_Platos_Tipo foreign key (codigoTipoPlato)
		references TipoPlato(codigoTipoPlato) on delete cascade
); 

Create table Productos_has_Platos(
	Productos_codigoProducto int not null, 
    codigoPlato int not null, 
    codigoProducto int not null, 
    primary key PK_Productos_codigoProducto (Productos_codigoProducto),
	constraint FK_Productos_has_Platos_Productos foreign key (codigoProducto) 
		references Productos(codigoProducto), 
	constraint FK_Productos_has_Platos_Platos foreign key (codigoPlato)
		references Platos(codigoPlato) on delete cascade
); 

Create table Servicios_has_Platos(
	Servicios_codigoServicio int not null, 
    codigoPlato int not null, 
    codigoServicio int not null, 
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio), 
    constraint FK_Servicios_has_Platos_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio), 
	constraint FK_Servicios_has_Platos_Platos foreign key (codigoPlato) 
		references Platos(codigoPlato) on delete cascade
);

Create table Servicios_has_Empleados( 
	Servicios_codigoServicio int not null, 
    codigoServicio int not null, 
    codigoEmpleado int not null, 
    fechaEvento date not null, 
    horaEvento time not null, 
    lugarEvento varchar(150) not null, 
    primary key PK_Servicios_codigoServicio (Servicios_codigoServicio), 
    constraint FK_Servicios_has_Empleados_Servicios foreign key (codigoServicio)
		references Servicios(codigoServicio), 
	constraint FK_Servicios_has_Empleados_Empleados foreign key (codigoEmpleado) 
		references Empleados(codigoEmpleado) on delete cascade
); 


Create table Usuario(
	codigoUsuario int not null auto_increment,
    nombreUsuario varchar (100) not null,
    apellidoUsuario varchar (100) not null,
    usuarioLogin varchar (50) not null,
    contrasena varchar (50) not null,
    primary key PK_codigoUsuario(codigoUsuario)
);

Delimiter $$
	Create procedure sp_AgregarUsuario(in nombreUsuario varchar(100), in apellidoUsuario varchar (100), in usuarioLogin varchar (50), in contrasena varchar (50))
		Begin
			Insert into Usuario (nombreUsuario, apellidoUsuario, usuarioLogin, contrasena)
				values(nombreUsuario, apellidoUsuario, usuarioLogin, contrasena);
		End $$
Delimiter ;


Delimiter $$
	Create procedure sp_ListarUsuarios()
		Begin
			Select 
             U.codigoUsuario,
             U.nombreUsuario,
             U.apellidoUsuario,
             U.usuarioLogin,
             U.contrasena
             from Usuario U;
		End $$
Delimiter ;
call sp_AgregarUsuario('Angel','Rodriguez','arodriguez-2019620@kinal.edu.gt','1234');
Call sp_ListarUsuarios();


Create table Login(
	usuarioMaster varchar(50) not null,
    passwordLogin varchar(50) not null,
    Primary key PK_usuarioMaster (usuarioMaster)
);

delimiter $$
create procedure sp_ReporteFinal()
begin
   select 
		*
		from 
			Empresas e 
			inner join servicios s ON s.codigoempresa = e.codigoempresa
				inner join presupuestos p ON p.codigoempresa = e.codigoempresa
					inner join servicios_has_empleados she ON she.codigoservicio = s.codigoservicio
						inner join empleados emp ON she.codigoempleado = emp.codigoempleado
							inner join tipoempleado te ON te.codigoTipoEmpleado = emp.codigotipoempleado
								inner join servicios_has_platos shp ON shp.codigoservicio = s.codigoservicio
									inner join platos pla ON pla.codigoplato = shp.codigoplato
										inner join tipoplato tp ON tp.codigotipoplato = pla.codigotipoplato
											inner join productos_has_platos php ON php.codigoplato = pla.codigoplato
												inner join productos pro ON pro.codigoproducto = php.codigoproducto;
end $$
delimiter ;
call sp_ReporteFinal();

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS ----------------------------------
-- ------------------------------------------Agregar Empresa----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarEmpresa(in nombreEmpresa varchar (150), in direccion varchar (150), in telefono varchar(8))
		Begin 
			Insert into Empresas(nombreEmpresa, direccion, telefono)
				values(nombreEmpresa, direccion, telefono); 
        End$$
Delimiter ;
-- -------------------------------------------Listar Empresa-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarEmpresas()
	Begin 
		Select 
			E.codigoEmpresa, 
			E.nombreEmpresa, 
            E.direccion, 
            E.telefono
            From Empresas E; 
    End$$
Delimiter ;
-- ------------------------------------------Buscar Empresa ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarEmpresa(in codEmpresa int)
		Begin 
			Select 
				E.codigoEmpresa, 
				E.nombreEmpresa, 
				E.direccion, ++
				E.telefono
				From Empresas E
					where codigoEmpresa = codEmpresa; 
        End$$
Delimiter ;
-- ------------------------------------------Eliminar Empresa --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarEmpresa(in codEmpresa int) 
		Begin 
			Delete from Empresas where codigoEmpresa = codEmpresa; 
        End$$
Delimiter ;
-- -------------------------------------------Editar Empresa -----------------------------------------------------
Delimiter $$
	Create procedure sp_EditarEmpresa(in codEmpresa int, in nomEmpresa varchar(150), in direc varchar(105), in tel varchar (8))
		Begin 
			Update Empresas E
				set 
				E.nombreEmpresa = nomEmpresa, 
                E.direccion = direc,
                E.telefono = tel 
				where codigoEmpresa =  codEmpresa;  
        End$$
Delimiter ;
-- ---------------DATOS
call sp_AgregarEmpresa('Pollo campero','9 Av 14-75 Ciudad de Guatemala 01013', '55511234');
call sp_AgregarEmpresa('McDonalds','Calzada Roosevelt zona 3 Mixco, Mixco', '31232113');
call sp_AgregarEmpresa('Burger King','Zona 7 Colonia Landivar', '98761723');
call sp_AgregarEmpresa('Taco bell','Zona 7 Colonia Landivar', '98761723');
call sp_AgregarEmpresa('Burger King','Zona 7 Colonia Landivar', '98761723');
call sp_ListarEmpresas();
call sp_BuscarEmpresa(1);
-- call sp_EliminarEmpresa(2);
call sp_EditarEmpresa(3,'Tonys Kinal 3', 'Zona 1 Sexta Avenida 18 calle', '45122014'); 

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE PRESUPUESTO ----------------------------------
-- ---------------------------------------------Agregar Presupuesto----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarPresupuesto(in fechaSolicitud date, in cantidadPresupuesto decimal(10,2), in codigoEmpresa int)
		Begin 
			Insert into Presupuestos(fechaSolicitud, cantidadPresupuesto, codigoEmpresa)
				values(fechaSolicitud, cantidadPresupuesto, codigoEmpresa); 
        End$$
Delimiter ;
-- -------------------------------------------Listar Presupuesto-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarPresupuestos()
	Begin 
		Select 
			P.codigoPresupuesto, 
            P.fechaSolicitud, 
            P.cantidadPresupuesto, 
            P.codigoEmpresa
            From Presupuestos P; 
    End$$
Delimiter ;

call sp_ListarPresupuestos();
-- ------------------------------------------Buscar Presupuesto ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarPresupuesto(in codEmpresa int)
		Begin 
			Select 
				P.codigoEmpresa
				From Presupuestos P
					where P.codigoEmpresa = codEmpresa; 
        End$$
Delimiter ;

-- call sp_BuscarPresupuesto();
-- ------------------------------------------Eliminar Presupuesto --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarPresupuesto(in codPresupuesto int) 
		Begin 
			delete from Presupuestos where codigoPresupuesto = codPresupuesto;
        End$$
Delimiter ;
-- -------------------------------------------Editar Presupuesto -----------------------------------------------------
Delimiter $$
	Create procedure sp_EditarPresupuesto(in codPresupuesto int, in fechaSoli date, in cantPresupuesto decimal(10,2),
		in codEmpresa int)
		Begin 
			Update Presupuestos P
				set 
				P.fechaSolicitud = fechaSoli, 
				P.cantidadPresupuesto = cantPresupuesto,
                P.codigoEmpresa = codEmpresa
				where codigoPresupuesto = codPresupuesto;   
        End$$
Delimiter ;
-- ---------------DATOS
call sp_AgregarPresupuesto('2022-01-28','3500.00', 1);
call sp_AgregarPresupuesto('2022-01-28','3500.00', 2);
call sp_AgregarPresupuesto('2022-01-28','3500.00', 3);
call sp_AgregarPresupuesto('2023-05-28','3320.00', 4);
call sp_AgregarPresupuesto('2023-05-28','3320.00', 5);
call sp_ListarPresupuestos();
call sp_BuscarPresupuesto(1);
-- call sp_EliminarPresupuesto(2);
call sp_EditarPresupuesto(3,'2021-02-12', '1000.00',1);

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE TIPOEMPLEADO ----------------------------------
-- ---------------------------------------------Agregar TipoEmpleado----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarTipoEmpleado(in descripcion varchar(50))
		Begin 
			Insert into TipoEmpleado(descripcion)
				values(descripcion); 
        End$$
Delimiter ;
-- -------------------------------------------Listar TipoEmpleados-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarTipoEmpleados()
	Begin 
		Select 
			T.codigoTipoEmpleado, 
            T.descripcion
            From TipoEmpleado T; 
    End$$
Delimiter ;
-- ------------------------------------------Buscar TipoEmpleados ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarTipoEmpleado(in codTipoEmpleado int)
	Begin 
		Select 
			T.codigoTipoEmpleado,
            T.descripcion
            From TipoEmpleado T
			where codigoTipoEmpleado = codTipoEmpleado; 
    End$$
Delimiter ;
-- ------------------------------------------Eliminar TipoEmpleado --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarTipoEmpleado(in codTipoEmpleado int) 
		Begin 
			Delete from TipoEmpleado	
				where codigoTipoEmpleado = codTipoEmpleado; 
        End$$
Delimiter ;
-- -------------------------------------------Editar TipoEmpleado -----------------------------------------------------
Delimiter $$
	Create procedure sp_EditarTipoEmpleado(in codTipoEmpleado int, in descri varchar(50))
		Begin 
			Update TipoEmpleado T
				set 
				T.descripcion = descri 
				where codigoTipoEmpleado =  codTipoEmpleado;  
        End$$
Delimiter ; 
-- ---------------DATOS
call sp_AgregarTipoEmpleado('Chef');
call sp_AgregarTipoEmpleado('Camarero o mesero');
call sp_AgregarTipoEmpleado('Bartender');
call sp_AgregarTipoEmpleado('Cocinero');
call sp_AgregarTipoEmpleado('Host o anfitrión');
call sp_ListarTipoEmpleados();
call sp_BuscarTipoEmpleado(1);
-- call sp_EliminarTipoEmpleado(2);
call sp_EditarTipoEmpleado(3,'Ayudante');
-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE TIPOPLATO ----------------------------------
-- ---------------------------------------------Agregar TipoPlato----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarTipoPlato(in descripcionTipo varchar(50))
		Begin 
			Insert into TipoPlato(descripcionTipo)
				values(descripcionTipo); 
        End$$
Delimiter ;
-- -------------------------------------------Listar TipoPlatos-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarTipoPlatos()
		Begin 
			Select 
				T.codigoTipoPlato, 
				T.descripcionTipo
				From TipoPlato T; 
		End$$
Delimiter ;
-- ------------------------------------------Buscar TipoPlato ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarTipoPlato(in codTipoPlato int)
		Begin 
			Select 
				T.codigoTipoPlato,
				T.descripcionTipo
				From TipoPlato T
				where codigoTipoPlato = codTipoPlato; 
		End$$
Delimiter ;
-- ------------------------------------------Eliminar TipoPlato --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarTipoPlato(in codTipoPlato int) 
		Begin 
			Delete from TipoPlato 	
				where codigoTipoPlato = codTipoPlato; 
        End$$
Delimiter ;
-- -------------------------------------------Editar TipoPlato -----------------------------------------------------
Delimiter $$
	Create procedure sp_EditarTipoPlato(in codTipoPlato int, in descriTipo varchar(50))
		Begin 
			Update TipoPlato T
				set 
				T.descripcionTipo = descriTipo
				where codigoTipoPlato =  codTipoPlato;  
        End$$
Delimiter ;
-- ---------------DATOS
call sp_AgregarTipoPlato('Plato entrada');
call sp_AgregarTipoPlato('Postre');
call sp_AgregarTipoPlato('Plato vegetariano');
call sp_AgregarTipoPlato('Plato principal');
call sp_AgregarTipoPlato('Plato de mariscos');
call sp_ListarTipoPlatos();
call sp_BuscarTipoPlato(1);
-- call sp_EliminarTipoPlato(2);
call sp_EditarTipoPlato(3,'Plato fuerte');

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE SERVICIOS ----------------------------------
-- ---------------------------------------------Agregar Servicio----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarServicio(in fechaServicio date, in tipoServicio varchar(50), in horaServicio time, 
		in lugarServicio varchar(150),in telefonoContacto varchar(150), in codigoEmpresa int)
		Begin 
			Insert into Servicios(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa)
				values(fechaServicio, tipoServicio, horaServicio, lugarServicio, telefonoContacto, codigoEmpresa); 
        End$$
Delimiter ;
-- -------------------------------------------Listar Servicios-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarServicios()
		Begin 
			Select 
				S.codigoServicio,
				S.fechaServicio, 
				S.tipoServicio, 
				S.horaServicio, 
				S.lugarServicio, 
				S.telefonoContacto, 
				S.codigoEmpresa
				From Servicios S; 
		End$$
Delimiter ;
-- ------------------------------------------Buscar Servicio ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarServicio(in codServicio int)
	Begin 
		Select 
			S.codigoServicio,
			S.fechaServicio, 
            S.tipoServicio, 
            S.horaServicio, 
            S.lugarServicio, 
            S.telefonoContacto, 
            S.codigoEmpresa
            From Servicios S
			where codigoServicio = codServicio; 
    End$$
Delimiter ;
-- ------------------------------------------Eliminar Servicio --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarServicio(in codServicio int) 
		Begin 
			Delete from Servicios 	
				where codigoServicio = codServicio; 
        End$$
Delimiter ;
-- -------------------------------------------Editar Servicio -----------------------------------------------------
Delimiter $$
	Create procedure sp_EditarServicio(in codServicio int, in fechaServi date, in tipoServi varchar(150), 
		in horaServi time, in lugarServi varchar(150), in telContacto varchar(150))
		Begin 
			Update Servicios S
				set 
				S.fechaServicio = fechaServi, 
				S.tipoServicio = tipoServi, 
				S.horaServicio = horaServi, 
				S.lugarServicio = lugarServi, 
				S.telefonoContacto = telContacto 
				where codigoServicio =  codServicio;  
        End$$
Delimiter ;
-- ---------------DATOS
call sp_AgregarServicio('2023-06-18', 'Boda', '12:30:00', 'Kilometro 13.5 Carretera al Salvador', '78419852', 1);
call sp_AgregarServicio('2023-06-18', 'Boda', '12:30:00', 'Kilometro 13.5 Carretera al Salvador', '78419852', 1);
call sp_AgregarServicio('2023-06-18', '15 años', '12:30:00', 'Kilometro 13.5 Carretera al Salvador', '78419852', 1);
call sp_AgregarServicio('2023-06-18', 'Boda', '12:30:00', 'Kilometro 13.5 Carretera al Salvador', '78419852', 1);
call sp_AgregarServicio('2023-06-18', '15 años', '12:30:00', 'Kilometro 13.5 Carretera al Salvador', '78419852', 1);
call sp_ListarServicios();
call sp_BuscarServicio(1);
-- call sp_EliminarServicio(2);
call sp_EditarServicio(3, '2023-04-20', 'Boda', '14:00:00', 'Zona 10, diagonal 6', '8623 1931');

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE PLATOS ----------------------------------
-- ---------------------------------------------Agregar Platos----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarPlato(in cantidad int, in nombrePlato varchar(150), in descripcion varchar(150), 
		in precioPlato decimal(10,2), in codigoTipoPlato int)
		Begin 
			Insert into Platos(cantidad, nombrePlato, descripcion, precioPlato, codigoTipoPlato)
				values(cantidad, nombrePlato, descripcion, precioPlato, codigoTipoPlato); 
        End$$
Delimiter ;
-- -------------------------------------------Listar Platos-------------------------------------------------------- 
Delimiter $$
	Create procedure sp_ListarPlatos()
	Begin 
		Select 
			P.codigoPlato, 
            P.cantidad, 
            P.nombrePlato,
            P.descripcion, 
            P.precioPlato, 
            P.codigoTipoPlato
            From Platos P; 
    End$$
Delimiter ;
-- ------------------------------------------Buscar Plato ------------------------------------------------
Delimiter $$
	Create procedure sp_BuscarPlato(in codPlato int)
	Begin 
		Select 
			P.codigoPlato, 
            P.cantidad, 
            P.nombrePlato,
            P.descripcion, 
            P.precioPlato, 
            P.codigoTipoPlato
            From Platos P
			where codigoPlato = codPlato; 
    End$$
Delimiter ;
-- ------------------------------------------Eliminar Plato --------------------------------------------------- 
Delimiter $$
	Create procedure sp_EliminarPlato(in codPlato int) 
		Begin 
			Delete from Platos 	
				where codigoPlato = codPlato; 
        End$$
Delimiter ;
-- -------------------------------------------Editar Plato -----------------------------------------------------
Delimiter //
	Create procedure sp_EditarPlato(in codPlato int, in cant int, in nomPlato varchar(150), in descri varchar(150), 
		in precPlato decimal(10,2))
		Begin 
			Update Platos P
				set  
				P.cantidad = cant, 
				P.nombrePlato = nomPlato,
				P.descripcion = descri, 
				P.precioPlato = precPlato
				where codigoPlato =  codPlato;  
        End//
Delimiter ;
-- ---------------DATOS
call sp_AgregarPlato(100, 'Lasagna de carne:', 'Capas de pasta intercaladas con carne molida sazonada', '80.00', 3);
call sp_AgregarPlato(100, 'Sushi de salmón', 'Deliciosos rollos de arroz sushi rellenos de salmón fresco', '100.00', 3);
call sp_AgregarPlato(100, 'Tacos al pastor', 'Tortillas de maíz rellenas de carne de cerdo marinada con especias', '45.00', 3);
call sp_AgregarPlato(100, 'Pad Thai', 'Fideos de arroz salteados con camarones', '65.00', 3);
call sp_AgregarPlato(100, 'Pollo al curry', 'Trozos de pollo tierno cocinados en una salsa aromática de curry', '40.00', 3);
call sp_ListarPlatos();
call sp_BuscarPlato(1);
-- call sp_EliminarPlato(2);
call sp_EditarPlato(3, 200, 'Carne a las brazas', 'Carne a las brazas con arroz y ensalada', '40.00');

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE PRODUCTOS ----------------------------------
-- ---------------------------------------------Agregar Producto------------------------------------------------
Delimiter //
	Create procedure sp_AgregarProducto(in nombreProducto varchar(150), in cantidad int)
		Begin
			Insert into Productos (nombreProducto, cantidad)
				Values(nombreProducto, cantidad);
        End//
Delimiter ;
-- ------------------------------------------------Listar Productos--------------------------------------------------- 
Delimiter //
	Create procedure sp_ListarProductos()
		Begin
			Select 
				P.codigoProducto,
				P.nombreProducto,
                P.cantidad
                From Productos P;
        End//
Delimiter ;
-- -------------------------------------------------Buscar Producto ------------------------------------------------
Delimiter //
	Create procedure sp_BuscarProducto(in codProducto int)
		Begin
			Select 
				P.codigoProducto,
				P.nombreProducto, 
                P.cantidad
                From Productos P
                Where codigoProducto = codProducto;
        End//
Delimiter ;
-- ------------------------------------------------Eliminar Producto --------------------------------------------------- 
Delimiter //
	Create  procedure sp_EliminarProducto(in codProducto int)
		Begin
			delete from Productos
            where codigoProducto = codProducto;
        End//
Delimiter ;
-- ------------------------------------------------Editar Producto -----------------------------------------------------
Delimiter //
	Create procedure sp_EditarProducto(in codProducto int, in nomProducto varchar(150), in cant int)	
		Begin
			Update Productos P
				set
                    P.nombreProducto = nomProducto, 
                    P.cantidad = cant
                    Where codigoProducto = codProducto;
		End //
Delimiter ;
-- DATOS ---------------
call sp_AgregarProducto('Ensalada Caprese', 27);
call sp_AgregarProducto('Filete de Salmón', 94);
call sp_AgregarProducto('Risotto de Champiñones', 63);
call sp_AgregarProducto('Pollo Marsala', 12);
call sp_AgregarProducto('Tarta de Manzana', 58);
call sp_ListarProductos();
call sp_BuscarProducto(1);
-- call sp_EliminarProducto(2);
call sp_EditarProducto(3, 'Pan con salchicha y papa', 100);
-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE PRODUCTOS ----------------------------------
-- ---------------------------------------------Agregar Empleado------------------------------------------------
Delimiter //
	Create procedure sp_AgregarEmpleado(in numeroEmpleado int,in apellidosEmpleado varchar(150),in nombresEmpleado varchar(150), 
		in direccionEmpleado varchar(150), in telefonoContacto varchar(8), in gradoCocinero varchar(50), in codigoTipoEmpleado int)
		Begin
			Insert into Empleados (numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero,
				codigoTipoEmpleado)
				Values(numeroEmpleado, apellidosEmpleado, nombresEmpleado, direccionEmpleado, telefonoContacto, gradoCocinero, 
					codigoTipoEmpleado);
        End//
Delimiter ;
-- ------------------------------------------------Listar Empleados--------------------------------------------------- 
Delimiter //
	Create procedure sp_ListarEmpleados()
		Begin
			Select 
				E.codigoEmpleado,
				E.numeroEmpleado, 
                E.apellidosEmpleado, 
                E.nombresEmpleado, 
                E.direccionEmpleado, 
                E.telefonoContacto, 
                E.gradoCocinero, 
                E.codigoTipoEmpleado
                from Empleados E;
        End//
Delimiter ;
-- -------------------------------------------------Buscar Empleado ------------------------------------------------
Delimiter //
	Create procedure sp_BuscarEmpleado(in codEmpleado int)
		Begin
			Select 
				E.codigoEmpleado, 
				E.numeroEmpleado, 
                E.apellidosEmpleado, 
                E.nombresEmpleado, 
                E.direccionEmpleado, 
                E.telefonoContacto, 
                E.gradoCocinero, 
                E.codigoTipoEmpleado
                from Empleados E
                where codigoEmpleado = codEmpleado;
        End//
Delimiter ;
-- ------------------------------------------------Eliminar Empleado --------------------------------------------------- 
Delimiter //
	Create  procedure sp_EliminarEmpleado(in codEmpleado int)
		Begin
			Delete From Empleados
            Where codigoEmpleado = codEmpleado;
        End//
Delimiter ;
-- ------------------------------------------------Editar Empleado --------------------------------------------------- 
Delimiter //
	Create procedure sp_EditarEmpleado(in codEmpleado int, in numEmpleado int, in apelliEmpleado varchar(150),
		in nomEmpleado varchar(150), in direcEmpleado varchar(150), in telContacto varchar(8), in graCoci varchar(50), in codTipoEmpleado int)
        Begin
			Update Empleados E
				set
                E.numeroEmpleado = numEmpleado,
                E.apellidosEmpleado = apelliEmpleado, 
                E.nombresEmpleado = nomEmpleado, 
                E.direccionEmpleado = direcEmpleado,
                E.telefonoContacto = telContacto,
                E.gradoCocinero = graCoci,
                E.codigoTipoEmpleado = codTipoEmpleado
                where E.codigoEmpleado = codEmpleado; 		
        End//
 Delimiter ; 
-- Llamada 1
call sp_AgregarEmpleado (1, 'González Pérez', 'Ana María', 'Avenida Principal, 123', '555-1234', 'Asistente Administrativo', 1); 
call sp_AgregarEmpleado (2, 'Martínez López', 'Carlos', 'Calle Secundaria, 456', '555-5678', 'Cocinero', 2);
call sp_AgregarEmpleado (3, 'Rodríguez Morales', 'Laura', 'Paseo Central, 789', '555-9876', 'Bartender', 3);
call sp_AgregarEmpleado (4, 'Hernández Gómez', 'Pedro', 'Plaza del Sol, 234', '555-2468', 'Técnico de soporte', 4);
call sp_AgregarEmpleado (5, 'López García', 'María', 'Avenida Principal, 567', '555-1357', 'Gerente', 5);
call sp_ListarEmpleados	();
call sp_BuscarEmpleado(1);
-- call sp_EliminarEmpleado(2);
call sp_EditarEmpleado(3, 5, 'Marroquin Santos', 'Fernanda Stacy', 'Zona 8, 8va calle', '51698732', 'Chef de cocina fría', 3);

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE PRODUCTOS_HAS_PLATOS ----------------------------------
-- ---------------------------------------------Agregar PRODUCTO_HAS_PLATO----------------------------------------------
Delimiter $$
	Create procedure sp_AgregarProducto_has_Plato(in Productos_codigoProducto int, in codigoPlato int, in codigoProducto int)
        begin
			Insert Into Productos_has_Platos(Productos_codigoProducto, codigoPlato, codigoProducto)
				Values (Productos_codigoProducto, codigoPlato, codigoProducto);
        end $$
Delimiter ; 

-- ---------------------------------------------Listar PRODUCTOS_HAS_PLATOS----------------------------------------------
Delimiter //
	Create procedure sp_ListarProductos_has_Platos()
		Begin
			Select 
				PHP.Productos_codigoProducto, 
				PHP.codigoPlato, 
				PHP.codigoProducto
				From Productos_has_Platos PHP; 
        End//
Delimiter ; 
-- ---------------------------------------------Buscar PRODUCTO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_BuscarProducto_has_Plato(in Producto_codProducto int)
		Begin
			Select 
				PHP.codigoPlato, 
				PHP.codigoProducto
				From Productos_has_Platos PHP
					Where PHP.Productos_codigoProducto = Producto_codProducto; 
        End//
Delimiter ; 
-- ---------------------------------------------Eliminar PRODUCTO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_EliminarProducto_has_Plato(in Productos_codProducto int)
		Begin
			Delete from Productos_has_Platos PHP
				Where PHP.Productos_codigoProducto = Productos_codProducto; 
        End//
Delimiter ; 
-- ---------------------------------------------Editar PRODUCTO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_EditarProducto_has_Plato(in Productos_codProducto int, 
		in codPlato int, in codProducto int)
		Begin
			update Productos_has_Platos PHP
				set 
					PHP.codigoPlato = codPlato, 
                    PHP.codigoProducto = codProducto
                    Where PHP.Productos_codigoProducto = Productos_codProducto;
        End//
Delimiter ; 
-- DATOS ---------------
call sp_AgregarProducto_has_Plato(1, 1, 1);
call sp_AgregarProducto_has_Plato(2, 2, 2);
call sp_AgregarProducto_has_Plato(3, 3, 3);
call sp_AgregarProducto_has_Plato(4, 4, 4);
call sp_AgregarProducto_has_Plato(5, 5, 5);
call sp_ListarProductos_has_Platos();
-- call sp_BuscarProducto_has_Plato(1);
-- call sp_EliminarProducto(2);
-- call sp_ListarProductos_has_Platos(3, 'Pan con salchicha y papa', 100);

-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE SERVICIOS_HAS_EMPLEADOS ----------------------------------
-- ---------------------------------------------Agregar SERVICIO_HAS_EMPLEADO----------------------------------------------
Delimiter //
	create procedure sp_AgregarServicio_has_Empleado(in Servicios_codigoServicio int, in codigoServicio int,
		in codigoEmpleado int , in fechaEvento date ,in  horaEvento time, in lugarEvento varchar(150))
        Begin
			Insert into Servicios_has_Empleados(Servicios_codigoServicio, codigoServicio, 
				codigoEmpleado, fechaEvento, horaEvento, lugarEvento) values 
                (Servicios_codigoServicio, codigoServicio, codigoEmpleado, fechaEvento, horaEvento, lugarEvento);
        End//
Delimiter ;

-- ---------------------------------------------Listar SERVICIOS_HAS_EMPLEADOS----------------------------------------------
Delimiter //
	Create procedure sp_ListarServicios_has_Empleados()
		Begin
			Select
            SHE.Servicios_codigoServicio,
            SHE.codigoServicio, 
            SHE.codigoEmpleado,
            SHE.fechaEvento,
            SHE.horaEvento,
            SHE.lugarEvento
            From Servicios_has_Empleados SHE; 
        End//
Delimiter ; 
-- ---------------------------------------------Buscar SERVICIO_HAS_EMPLEADO----------------------------------------------
Delimiter //
	Create procedure sp_BuscarServicio_has_Empleado(in Servicio_codServicio int)
		Begin
			Select
            SHE.codigoServicio, 
            SHE.codigoEmpleado,
            SHE.fechaEvento,
            SHE.horaEvento,
            SHE.lugarEvento
            From Servicios_has_Empleados SHE
				Where SHE.Servicios_codigoServicio = Servicio_codServicio; 
				
        End//
Delimiter ; 
-- ---------------------------------------------Eliminar SERVICIO_HAS_EMPLEADO----------------------------------------------
Delimiter //
	Create procedure sp_EliminarServicio_has_Empleado(in Servicios_codServicio int )
		Begin
			Delete from Servicios_has_Empleados SHE 
				where SHE.Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ; 
-- ---------------------------------------------Editar SERVICIO_HAS_EMPLEADO----------------------------------------------
Delimiter //
	Create procedure sp_EditarServicio_has_Empleado(in Servicios_codServicio int, in fechaEvent date,
		in  horaEvent time, in lugarEvent varchar(150))
        Begin
			Update Servicios_has_Empleados SHE 
				set
					SHE.fechaEvento = fechaEvent, 
                    SHE.horaEvento = horaEvent, 
                    SHE.lugarEvento = lugarEvent
                    Where SHE.Servicios_codigoServicio = Servicios_codServicio; 
        End//
Delimiter ; 
call sp_AgregarServicio_has_Empleado(1, 1, 1, '2023-05-10', '12:30:00', "23");
call sp_AgregarServicio_has_Empleado(2, 2, 2, '2023-05-10', '12:30:00', "23");
call sp_AgregarServicio_has_Empleado(3, 3, 3, '2023-05-10', '12:30:00', "23");
call sp_AgregarServicio_has_Empleado(4, 4, 4, '2023-05-10', '12:30:00', '23');
call sp_AgregarServicio_has_Empleado(5, 5, 5, '2023-05-10', '12:30:00', '23');

call sp_ListarServicios_has_Empleados();
-- ------------------------------------PROCEDIMIENTOS ALMACENADOS DE SERVICIOS_HAS_PLATOS ----------------------------------
-- ---------------------------------------------Agregar SERVICIO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_AgregarServicio_has_Plato(in Servicios_codigoServicio int , in codigoPlato int,
		in codigoServicio int)
        Begin
			Insert into Servicios_has_Platos(Servicios_codigoServicio, codigoPlato, codigoServicio)
				Values (Servicios_codigoServicio, codigoPlato, codigoServicio);
        End//
Delimiter ;
-- ---------------------------------------------Listar SERVICIOS_HAS_PLATOS----------------------------------------------
Delimiter //
	Create procedure sp_ListarServicios_has_Platos()
		Begin
			Select 
				SHP.Servicios_codigoServicio,
                SHP.codigoPlato, 
                SHP.codigoServicio
                From Servicios_has_Platos SHP; 
        End//
Delimiter ;
-- ---------------------------------------------Buscar SERVICIO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_BuscarServicio_has_Plato (in Servicios_codProducto int)
		Begin
			Select 
                SHP.codigoPlato, 
                SHP.codigoServicio
                From Servicios_has_Platos SHP
					Where SHP.Servicios_codigoProducto = Servicios_codProducto; 
        End//
Delimiter ;
-- ---------------------------------------------Eliminar SERVICIO_HAS_PLATO----------------------------------------------
Delimiter //
	Create procedure sp_EliminarServicio_has_Plato(in Servicios_codProducto int)
		Begin
			Delete from Servicios_has_Platos SHP
				where SHP.Servicios_codigoProducto = Servicios_codProducto; 
        End//
Delimiter ;
-- ---------------------------------------------Editar SERVICIO_HAS_PLATO----------------------------------------------
Delimiter //
	Create Procedure sp_EditarServicio_has_Plato(in Servicios_codProducto int , in codPlato int,
		in codServicio int)
        Begin
			Update Servicios_has_Platos SHP
				set 
					SHP.codigoPlato = codPlato, 
					SHP.codigoServicio = codServicio
					Where SHP.Servicios_codigoProducto = Servicios_codProducto; 					
        End//
Delimiter ;
call sp_AgregarServicio_has_Plato (1, 1 ,1);
call sp_AgregarServicio_has_Plato (2, 2 ,2);
call sp_AgregarServicio_has_Plato (3, 3, 3);
call sp_AgregarServicio_has_Plato (4, 4 ,4);
call sp_AgregarServicio_has_Plato (5, 5, 5);
call sp_ListarServicios_has_Platos();
