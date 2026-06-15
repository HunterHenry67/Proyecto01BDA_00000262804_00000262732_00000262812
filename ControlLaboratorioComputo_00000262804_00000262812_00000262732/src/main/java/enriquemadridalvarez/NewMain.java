/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package enriquemadridalvarez;

import Dtos.CancelarReservaDTO;
import Dtos.FinalizarReservaDTO;
import Dtos.GuardarReservaDTO;
import Dtos.ObtenerCatalogoSoftwareDTO;
import Dtos.ComputadoraDTO;
import Entidades.Alumno;
import Entidades.Bloqueo;
import Entidades.CentroComputo;
import Entidades.Computadora;
import Entidades.Reserva;
import Entidades.Software;
import Entidades.UnidadAcademica;
import Persistencia.*;
import PresentacionProgramaApartado.frmPantallaPrincipal;
import PresentacionProgramaApartado.frmSeleccionEquipo;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Pruebas manuales de todos los métodos DAO.
 * Ajusta las constantes según los datos que tengas en tu BD.
 */
public class NewMain {

    static final int    ID_ALUMNO           = 1;
    static final int    ID_COMPUTADORA      = 1;
    static final int    ID_CENTRO_COMPUTO   = 1;
    static final int    ID_UNIDAD           = 1;
    static final String IP_COMPUTADORA      = "192.168.1.1";
    static final String CONTRASENA_MAESTRA  = "admin123";
    static final String CONTRASENA_ALUMNO   = "pass123";

    public static void main(String[] args) {
//        IConexionBD conexion = new ConexionBD();
//
//        probarAlumnoDAO(conexion);
//        probarBloqueoDAO(conexion);
//        probarCentroComputoDAO(conexion);
//        probarComputadoraDAO(conexion);
//        probarReservaDAO(conexion);
//        probarSoftwareDAO(conexion);
//        probarUnidadAcademicaDAO(conexion);
        probarFarme();
    }

    // =========================================================
    // ALUMNO DAO
    // =========================================================
    static void probarAlumnoDAO(IConexionBD conexion) {
        System.out.println("\n========== ALUMNO DAO ==========");
        IAlumnoDAO dao = new AlumnoDAO(conexion);

        // consultar(filtro)
        try {
            List<Alumno> alumnos = dao.consultar("Garcia");
            System.out.println("[consultar] Alumnos con 'Garcia': " + alumnos.size());
            alumnos.forEach(a -> System.out.println("  -> " + a.getNombres() + " " + a.getApellidoPaterno()));
        } catch (PersistenciaException e) {
            System.out.println("[consultar] ERROR: " + e.getMessage());
        }

        // consultarAlumnoPorID
        try {
            Alumno alumno = dao.consultarAlumnoPorID(ID_ALUMNO);
            if (alumno != null) {
                System.out.println("[consultarAlumnoPorID] Encontrado: " + alumno.getNombres() + " " + alumno.getApellidoPaterno());
            } else {
                System.out.println("[consultarAlumnoPorID] No encontrado con ID: " + ID_ALUMNO);
            }
        } catch (PersistenciaException e) {
            System.out.println("[consultarAlumnoPorID] ERROR: " + e.getMessage());
        }

        // estaBloqueado
        try {
            boolean bloqueado = dao.estaBloqueado(ID_ALUMNO);
            System.out.println("[estaBloqueado] Alumno " + ID_ALUMNO + " bloqueado: " + bloqueado);
        } catch (PersistenciaException e) {
            System.out.println("[estaBloqueado] ERROR: " + e.getMessage());
        }

        // consultarCredenciales
        try {
            Alumno alumno = dao.consultarCredenciales(ID_ALUMNO, CONTRASENA_ALUMNO);
            if (alumno != null) {
                System.out.println("[consultarCredenciales] Credenciales válidas: " + alumno.getNombres());
            } else {
                System.out.println("[consultarCredenciales] Credenciales incorrectas.");
            }
        } catch (PersistenciaException e) {
            System.out.println("[consultarCredenciales] ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // BLOQUEO DAO
    // =========================================================
    static void probarBloqueoDAO(IConexionBD conexion) {
        System.out.println("\n========== BLOQUEO DAO ==========");
        IBloqueoDAO dao = new BloqueoDAO(conexion);

        // registrarBloqueo
        Bloqueo bloqueo = new Bloqueo();
        bloqueo.setFechaHoraIncioBloqueo(LocalDateTime.now());
        bloqueo.setMotivo("Prueba de bloqueo");
        bloqueo.setIdAlumno(ID_ALUMNO);
        try {
            Bloqueo registrado = dao.registrarBloqueo(bloqueo);
            System.out.println("[registrarBloqueo] Bloqueo registrado con ID: " + registrado.getIdBloqueo());
        } catch (PersistenciaException e) {
            System.out.println("[registrarBloqueo] ERROR: " + e.getMessage());
        }

        // consultar(filtro)
        try {
            List<Bloqueo> bloqueos = dao.consultar("Prueba");
            System.out.println("[consultar] Bloqueos con filtro 'Prueba': " + bloqueos.size());
            bloqueos.forEach(b -> System.out.println("  -> ID: " + b.getIdBloqueo() + " | Motivo: " + b.getMotivo()));
        } catch (PersistenciaException e) {
            System.out.println("[consultar] ERROR: " + e.getMessage());
        }

        // consultarBloqueosActivos
        try {
            List<Bloqueo> activos = dao.consultarBloqueosActivos();
            System.out.println("[consultarBloqueosActivos] Bloqueos activos: " + activos.size());
            activos.forEach(b -> System.out.println("  -> Alumno ID: " + b.getIdAlumno() + " | Motivo: " + b.getMotivo()));
        } catch (PersistenciaException e) {
            System.out.println("[consultarBloqueosActivos] ERROR: " + e.getMessage());
        }

        // desbloquearAlumno
        try {
            dao.desbloquearAlumno(ID_ALUMNO);
            System.out.println("[desbloquearAlumno] Alumno " + ID_ALUMNO + " desbloqueado correctamente.");
        } catch (PersistenciaException e) {
            System.out.println("[desbloquearAlumno] ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // CENTRO COMPUTO DAO
    // =========================================================
    static void probarCentroComputoDAO(IConexionBD conexion) {
        System.out.println("\n========== CENTRO COMPUTO DAO ==========");
        ICentroComputoDAO dao = new CentroComputoDAO(conexion);

        // obtenerPorID
        try {
            CentroComputo cc = dao.obtenerPorID(ID_CENTRO_COMPUTO);
            if (cc != null) {
                System.out.println("[obtenerPorID] Horario: " + cc.getHoraInicio() + " - " + cc.getHOranFin());
            } else {
                System.out.println("[obtenerPorID] No encontrado con ID: " + ID_CENTRO_COMPUTO);
            }
        } catch (PersistenciaException e) {
            System.out.println("[obtenerPorID] ERROR: " + e.getMessage());
        }

        // obtenerTodos
        try {
            List<CentroComputo> todos = dao.obtenerTodos();
            System.out.println("[obtenerTodos] Centros registrados: " + todos.size());
            todos.forEach(c -> System.out.println("  -> ID: " + c.getIdCentroComputo() + " | Unidad: " + c.getIdUnidadAcademica()));
        } catch (PersistenciaException e) {
            System.out.println("[obtenerTodos] ERROR: " + e.getMessage());
        }

        // obtenerPorUnidadAcademica
        try {
            List<CentroComputo> porUnidad = dao.obtenerPorUnidadAcademica(ID_UNIDAD);
            System.out.println("[obtenerPorUnidadAcademica] Centros en unidad " + ID_UNIDAD + ": " + porUnidad.size());
        } catch (PersistenciaException e) {
            System.out.println("[obtenerPorUnidadAcademica] ERROR: " + e.getMessage());
        }

        // obtenerPorComputadora
        try {
            CentroComputo cc = dao.obtenerPorComputadora(ID_COMPUTADORA);
            if (cc != null) {
                System.out.println("[obtenerPorComputadora] Centro de PC " + ID_COMPUTADORA + ": ID " + cc.getIdCentroComputo());
            } else {
                System.out.println("[obtenerPorComputadora] No encontrado para PC: " + ID_COMPUTADORA);
            }
        } catch (PersistenciaException e) {
            System.out.println("[obtenerPorComputadora] ERROR: " + e.getMessage());
        }

        // validarContraseniaMaestra
        try {
            boolean valida = dao.validarContraseniaMaestra(CONTRASENA_MAESTRA);
            System.out.println("[validarContraseniaMaestra] '" + CONTRASENA_MAESTRA + "' válida: " + valida);
        } catch (PersistenciaException e) {
            System.out.println("[validarContraseniaMaestra] ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // COMPUTADORA DAO
    // =========================================================
    static void probarComputadoraDAO(IConexionBD conexion) {
        System.out.println("\n========== COMPUTADORA DAO ==========");
        IComputadoraDAO dao = new ComputadoraDAO(conexion);

        // obtenerPCPorIP
        try {
            Computadora pc = dao.obtenerPCPorIP(IP_COMPUTADORA);
            if (pc != null) {
                System.out.println("[obtenerPCPorIP] PC encontrada | Máquina #" + pc.getNumeroMaquina() + " | Estatus: " + pc.isEstatus());
            } else {
                System.out.println("[obtenerPCPorIP] No encontrada con IP: " + IP_COMPUTADORA);
            }
        } catch (PersistenciaException e) {
            System.out.println("[obtenerPCPorIP] ERROR: " + e.getMessage());
        }

        // obtenerCatalogoSoftwarePC
        try {
            ComputadoraDTO pc = dao.obtenerCatalogoSoftwarePC(ID_COMPUTADORA);
            if (pc != null) {
                System.out.println("[obtenerCatalogoSoftwarePC] Software en PC " + ID_COMPUTADORA + ": " + pc.getCatalogoSoftware().size());
                pc.getCatalogoSoftware().forEach(s -> System.out.println("  -> " + s.getNombre()));
            } else {
                System.out.println("[obtenerCatalogoSoftwarePC] No encontrada con ID: " + ID_COMPUTADORA);
            }
        } catch (PersistenciaException e) {
            System.out.println("[obtenerCatalogoSoftwarePC] ERROR: " + e.getMessage());
        }

        // mostrarComputadoraApartada
        try {
            Computadora pc = dao.mostrarComputadoraApartada(ID_COMPUTADORA);
            if (pc != null) {
                System.out.println("[mostrarComputadoraApartada] PC apartada encontrada: #" + pc.getNumeroMaquina());
            } else {
                System.out.println("[mostrarComputadoraApartada] PC no está apartada o no existe.");
            }
        } catch (PersistenciaException e) {
            System.out.println("[mostrarComputadoraApartada] ERROR: " + e.getMessage());
        }

        // mostrarComputadoraComoDisponible
//        try {
//            dao.mostrarComputadoraComoDisponible(ID_COMPUTADORA);
//            System.out.println("[mostrarComputadoraComoDisponible] PC " + ID_COMPUTADORA + " marcada como disponible.");
//        } catch (PersistenciaException e) {
//            System.out.println("[mostrarComputadoraComoDisponible] ERROR: " + e.getMessage());
//        }
    }

    // =========================================================
    // RESERVA DAO
    // =========================================================
    static void probarReservaDAO(IConexionBD conexion) {
        System.out.println("\n========== RESERVA DAO ==========");
        IReservaDAO dao = new ReservaDAO(conexion);

        // guardar
        GuardarReservaDTO dto = new GuardarReservaDTO(
                LocalDateTime.now(), null, null, null, ID_ALUMNO, ID_COMPUTADORA);
        int idReservaCreada = -1;
        try {
            Reserva guardada = dao.guardar(dto);
            idReservaCreada = guardada.getIdReserva();
            System.out.println("[guardar] Reserva guardada con ID: " + idReservaCreada);
        } catch (PersistenciaException e) {
            System.out.println("[guardar] ERROR: " + e.getMessage());
        }

        // registrarReserva
        try {
            GuardarReservaDTO dto2 = new GuardarReservaDTO(
                    LocalDateTime.now(), null, null, null, ID_ALUMNO, ID_COMPUTADORA);
            int id = dao.registrarReserva(dto2);
            System.out.println("[registrarReserva] Reserva registrada con ID: " + id);
            // cancelamos esta para no dejar basura
            dao.cancelarReserva(id);
        } catch (PersistenciaException e) {
            System.out.println("[registrarReserva] ERROR: " + e.getMessage());
        }

        // consultarReservaActivaPorAlumno
        try {
            Reserva activa = dao.consultarResrevaActivaPorAlumno(ID_ALUMNO);
            if (activa != null) {
                System.out.println("[consultarReservaActivaPorAlumno] Reserva activa ID: " + activa.getIdReserva());
            } else {
                System.out.println("[consultarReservaActivaPorAlumno] Sin reserva activa para alumno: " + ID_ALUMNO);
            }
        } catch (PersistenciaException e) {
            System.out.println("[consultarReservaActivaPorAlumno] ERROR: " + e.getMessage());
        }

        // consultarReservaActivaPorComputadora
        try {
            Reserva activa = dao.consultarReservaActivaPorComputadora(ID_COMPUTADORA);
            if (activa != null) {
                System.out.println("[consultarReservaActivaPorComputadora] Reserva activa en PC " + ID_COMPUTADORA + ": ID " + activa.getIdReserva());
            } else {
                System.out.println("[consultarReservaActivaPorComputadora] Sin reserva activa en PC: " + ID_COMPUTADORA);
            }
        } catch (PersistenciaException e) {
            System.out.println("[consultarReservaActivaPorComputadora] ERROR: " + e.getMessage());
        }

        // consultarReservasActivas
        try {
            List<Reserva> activas = dao.consultarReservasActivas();
            System.out.println("[consultarReservasActivas] Total activas: " + activas.size());
            activas.forEach(r -> System.out.println("  -> ID: " + r.getIdReserva() + " | Alumno: " + r.getIdAlumno()));
        } catch (PersistenciaException e) {
            System.out.println("[consultarReservasActivas] ERROR: " + e.getMessage());
        }

        // consultar(filtro)
        try {
            List<Reserva> todas = dao.consultar("");
            System.out.println("[consultar] Total reservas: " + todas.size());
        } catch (PersistenciaException e) {
            System.out.println("[consultar] ERROR: " + e.getMessage());
        }

        // consultarReservaPorID
        if (idReservaCreada != -1) {
            try {
                Reserva r = dao.consultarReservaPorID(idReservaCreada);
                if (r != null) {
                    System.out.println("[consultarReservaPorID] Reserva " + idReservaCreada + " | Alumno: " + r.getIdAlumno());
                } else {
                    System.out.println("[consultarReservaPorID] No encontrada con ID: " + idReservaCreada);
                }
            } catch (PersistenciaException e) {
                System.out.println("[consultarReservaPorID] ERROR: " + e.getMessage());
            }
        }

        // consultarMinutosUsadosPorAlumno
        try {
            int minutos = dao.consultarMinutosUsadosPorAlumno(ID_ALUMNO);
            System.out.println("[consultarMinutosUsadosPorAlumno] Minutos usados por alumno " + ID_ALUMNO + ": " + minutos);
        } catch (PersistenciaException e) {
            System.out.println("[consultarMinutosUsadosPorAlumno] ERROR: " + e.getMessage());
        }

        // finalizar (con FinalizarReservaDTO)
        if (idReservaCreada != -1) {
            try {
                FinalizarReservaDTO finalizarDTO = new FinalizarReservaDTO(idReservaCreada, LocalDateTime.now());
                Reserva finalizada = dao.finalizar(finalizarDTO);
                System.out.println("[finalizar] Reserva " + idReservaCreada + " finalizada. Fin: " + finalizada.getFechaHoraFinal());
            } catch (PersistenciaException e) {
                System.out.println("[finalizar] ERROR: " + e.getMessage());
            }
        }

        // finalizarReserva (void con FinalizarReservaDTO)
        try {
            GuardarReservaDTO dto3 = new GuardarReservaDTO(
                    LocalDateTime.now(), null, null, null, ID_ALUMNO, ID_COMPUTADORA);
            int idTemp = dao.registrarReserva(dto3);
            dao.finalizarReserva(new FinalizarReservaDTO(idTemp, LocalDateTime.now()));
            System.out.println("[finalizarReserva] Reserva " + idTemp + " finalizada correctamente.");
        } catch (PersistenciaException e) {
            System.out.println("[finalizarReserva] ERROR: " + e.getMessage());
        }

        // cancelar (con CancelarReservaDTO)
        try {
            GuardarReservaDTO dto4 = new GuardarReservaDTO(
                    LocalDateTime.now(), null, null, null, ID_ALUMNO, ID_COMPUTADORA);
            int idTemp = dao.registrarReserva(dto4);
            Reserva cancelada = dao.cancelar(new CancelarReservaDTO(idTemp));
            System.out.println("[cancelar] Reserva " + cancelada.getIdReserva() + " cancelada.");
        } catch (PersistenciaException e) {
            System.out.println("[cancelar] ERROR: " + e.getMessage());
        }

        // cancelarReserva (void con int)
        try {
            GuardarReservaDTO dto5 = new GuardarReservaDTO(
                    LocalDateTime.now(), null, null, null, ID_ALUMNO, ID_COMPUTADORA);
            int idTemp = dao.registrarReserva(dto5);
            dao.cancelarReserva(idTemp);
            System.out.println("[cancelarReserva] Reserva " + idTemp + " cancelada correctamente.");
        } catch (PersistenciaException e) {
            System.out.println("[cancelarReserva] ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // SOFTWARE DAO
    // =========================================================
    static void probarSoftwareDAO(IConexionBD conexion) {
        System.out.println("\n========== SOFTWARE DAO ==========");
        ISoftwareDAO dao = new SoftwareDAO(conexion);

        // obtenerCatalogoSoftware
        try {
            ObtenerCatalogoSoftwareDTO dto = new ObtenerCatalogoSoftwareDTO(ID_COMPUTADORA);
            List<Software> catalogo = dao.obtenerCatalogoSoftware(dto);
            System.out.println("[obtenerCatalogoSoftware] Software en PC " + ID_COMPUTADORA + ": " + catalogo.size());
            catalogo.forEach(s -> System.out.println("  -> " + s.getNombre()));
        } catch (PersistenciaException e) {
            System.out.println("[obtenerCatalogoSoftware] ERROR: " + e.getMessage());
        }
    }

    // =========================================================
    // UNIDAD ACADEMICA DAO
    // =========================================================
    static void probarUnidadAcademicaDAO(IConexionBD conexion) {
        System.out.println("\n========== UNIDAD ACADEMICA DAO ==========");
        IUnidadAcademicaDAO dao = new UnidadAcademicaDAO(conexion);

        // consultarUnidadAcademicaPorID
        try {
            UnidadAcademica unidad = dao.consultarUnidadAcademicaPorID(ID_UNIDAD);
            if (unidad != null) {
                System.out.println("[consultarUnidadAcademicaPorID] Unidad: " + unidad.getNombre());
            } else {
                System.out.println("[consultarUnidadAcademicaPorID] No encontrada con ID: " + ID_UNIDAD);
            }
        } catch (PersistenciaException e) {
            System.out.println("[consultarUnidadAcademicaPorID] ERROR: " + e.getMessage());
        }

        // consultarUnidadesAcademicas(filtro)
        try {
            List<UnidadAcademica> unidades = dao.consultarUnidadesAcademicas("");
            System.out.println("[consultarUnidadesAcademicas] Total unidades: " + unidades.size());
            unidades.forEach(u -> System.out.println("  -> ID: " + u.getIdUnidadAcademica() + " | Nombre: " + u.getNombre()));
        } catch (PersistenciaException e) {
            System.out.println("[consultarUnidadesAcademicas] ERROR: " + e.getMessage());
        }
    }
    
    static void probarFarme(){
        frmPantallaPrincipal prueba = new frmPantallaPrincipal();
        prueba.setVisible(true);
    }
    
    
}
