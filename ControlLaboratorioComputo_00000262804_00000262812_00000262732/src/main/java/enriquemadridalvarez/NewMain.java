/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package enriquemadridalvarez;

import Entidades.CentroComputo;
import Negocio.CentroComputoBO;
import Negocio.NegocioException;
import Persistencia.ICentroComputoDAO;
import Persistencia.IConexionBD;
import Persistencia.PersistenciaException;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class NewMain {

    public static void main(String[] args) {
        try {
            ICentroComputoDAO dao = new CentroComputoDAOFake();
            CentroComputoBO centroBO = new CentroComputoBO(dao);

            probarHorarioCentroAbierto(centroBO);
            probarCentroInexistente(centroBO);
            probarContraseniaCorrecta(centroBO);
            probarContraseniaVacia(centroBO);
            probarObtenerCentrosPorUnidad(centroBO);
            probarObtenerCentroPorComputadora(centroBO);

        } catch (Exception e) {
            System.out.println("Error general en pruebas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void probarHorarioCentroAbierto(CentroComputoBO centroBO) throws NegocioException {
        boolean resultado = centroBO.validarHorarioServicio(1);

        if (resultado) {
            System.out.println("OK - Centro abierto dentro del horario");
        } else {
            System.out.println("FALLO - El centro debería estar abierto");
        }
    }

    private static void probarCentroInexistente(CentroComputoBO centroBO) {
        try {
            centroBO.validarHorarioServicio(999);
            System.out.println("FALLO - Debió lanzar excepción por centro inexistente");
        } catch (NegocioException e) {
            System.out.println("OK - Centro inexistente detectado");
        }
    }

    private static void probarContraseniaCorrecta(CentroComputoBO centroBO) throws NegocioException {
        boolean resultado = centroBO.validarContraseniaMaestra("admin123");

        if (resultado) {
            System.out.println("OK - Contraseña maestra correcta");
        } else {
            System.out.println("FALLO - La contraseña debería ser válida");
        }
    }

    private static void probarContraseniaVacia(CentroComputoBO centroBO) {
        try {
            centroBO.validarContraseniaMaestra("");
            System.out.println("FALLO - Debió lanzar excepción por contraseña vacía");
        } catch (NegocioException e) {
            System.out.println("OK - Contraseña vacía detectada");
        }
    }

    private static void probarObtenerCentrosPorUnidad(CentroComputoBO centroBO) throws NegocioException {
        List<CentroComputo> centros = centroBO.obtenerCentrosPorUnidad(1);

        if (centros != null && !centros.isEmpty()) {
            System.out.println("OK - Centros obtenidos por unidad académica");
        } else {
            System.out.println("FALLO - Debería regresar centros");
        }
    }

    private static void probarObtenerCentroPorComputadora(CentroComputoBO centroBO) throws NegocioException {
        CentroComputo centro = centroBO.obtenerCentroPorComputadora(10);

        if (centro != null) {
            System.out.println("OK - Centro obtenido por computadora");
        } else {
            System.out.println("FALLO - Debería regresar un centro");
        }
    }

    static class CentroComputoDAOFake implements ICentroComputoDAO {

        @Override
        public CentroComputo obtenerPorID(Integer idCentroComputo) throws PersistenciaException {
            if (idCentroComputo == 1) {
                return new CentroComputo(
                        1,
                        Time.valueOf("00:00:00"),
                        Time.valueOf("23:59:59"),
                        "admin123",
                        1
                );
            }
            return null;
        }

        @Override
        public List<CentroComputo> obtenerTodos() throws PersistenciaException {
            List<CentroComputo> centros = new ArrayList<>();

            centros.add(new CentroComputo(
                    1,
                    Time.valueOf("00:00:00"),
                    Time.valueOf("23:59:59"),
                    "admin123",
                    1
            ));

            centros.add(new CentroComputo(
                    2,
                    Time.valueOf("08:00:00"),
                    Time.valueOf("20:00:00"),
                    "maestra456",
                    1
            ));

            return centros;
        }

        @Override
        public List<CentroComputo> obtenerPorUnidadAcademica(Integer idUnidadAcademica) throws PersistenciaException {
            List<CentroComputo> centros = new ArrayList<>();

            if (idUnidadAcademica != null && idUnidadAcademica == 1) {
                centros.add(new CentroComputo(
                        1,
                        Time.valueOf("00:00:00"),
                        Time.valueOf("23:59:59"),
                        "admin123",
                        1
                ));
            }

            return centros;
        }

        @Override
        public CentroComputo obtenerPorComputadora(Integer idComputadora) throws PersistenciaException {
            if (idComputadora != null && idComputadora == 10) {
                return new CentroComputo(
                        1,
                        Time.valueOf("00:00:00"),
                        Time.valueOf("23:59:59"),
                        "admin123",
                        1
                );
            }
            return null;
        }

        @Override
        public boolean validarContraseniaMaestra(String contraseniaMaestra) throws PersistenciaException {
            return "admin123".equals(contraseniaMaestra);
        }
    }
}
