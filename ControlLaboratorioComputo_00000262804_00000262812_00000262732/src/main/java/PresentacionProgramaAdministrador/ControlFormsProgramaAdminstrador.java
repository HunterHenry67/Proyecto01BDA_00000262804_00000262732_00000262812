package PresentacionProgramaAdministrador;

import Negocio.AlumnoBO;
import Negocio.BloqueoBO;
import Negocio.CentroComputoBO;
import Negocio.ComputadoraBO;
import Negocio.IAlumnoBO;
import Negocio.IBloqueoBO;
import Negocio.ICentroComputoBO;
import Negocio.IComputadoraBO;
import Negocio.IReservaBO;
import Negocio.ReservaBO;
import Persistencia.AlumnoDAO;
import Persistencia.BloqueoDAO;
import Persistencia.CentroComputoDAO;
import Persistencia.ComputadoraDAO;
import Persistencia.ConexionBD;
import Persistencia.IConexionBD;
import Persistencia.ReservaDAO;

/**
 * Fábrica de objetos de negocio para el Programa Administrador.
 * Centraliza la creación de los BOs que usan los formularios del administrador.
 */
public class ControlFormsProgramaAdminstrador {

    private static ControlFormsProgramaAdminstrador instancia;

    private final IConexionBD conexionBD;
    private final IAlumnoBO alumnoBO;
    private final IBloqueoBO bloqueoBO;
    private final IReservaBO reservaBO;
    private final ICentroComputoBO centroComputoBO;
    private final IComputadoraBO computadoraBO;

    private ControlFormsProgramaAdminstrador() {
        this.conexionBD = new ConexionBD();
        this.alumnoBO  = new AlumnoBO(new AlumnoDAO(conexionBD));
        this.bloqueoBO = new BloqueoBO(new BloqueoDAO(conexionBD));
        this.reservaBO = new ReservaBO(new ReservaDAO(conexionBD));
        this.centroComputoBO = new CentroComputoBO(new CentroComputoDAO(conexionBD));
        this.computadoraBO = new ComputadoraBO(new ComputadoraDAO(conexionBD));
    }

    /** Singleton: devuelve la única instancia del controlador. */
    public static ControlFormsProgramaAdminstrador getInstance() {
        if (instancia == null) {
            instancia = new ControlFormsProgramaAdminstrador();
        }
        return instancia;
    }

    public IAlumnoBO getAlumnoBO() {
        return alumnoBO;
    }

    public IBloqueoBO getBloqueoBO() {
        return bloqueoBO;
    }

    public IReservaBO getReservaBO() {
        return reservaBO;
    }

    public ICentroComputoBO getCentroComputoBO() {
        return centroComputoBO;
    }

    public IComputadoraBO getComputadoraBO() {
        return computadoraBO;
    }
}
