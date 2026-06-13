/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dtos.ComputadoraDTO;
import Dtos.ReservaDTO;
import Entidades.Computadora;
import Persistencia.AlumnoDAO;
import Persistencia.ComputadoraDAO;
import Persistencia.IAlumnoDAO;
import Persistencia.IComputadoraDAO;
import Persistencia.IReservaDAO;
import Persistencia.PersistenciaException;
import Persistencia.ReservaDAO;

/**
 *
 * @author user
 */
public class BloqueadorBO {
    private final IComputadoraDAO computadoraDAO;
    private final IReservaDAO reservaDAO;
    private final IAlumnoDAO alumnoDAO;
    
    public BloqueadorBO() {
        this.computadoraDAO = new ComputadoraDAO();
        this.reservaDAO = new ReservaDAO();
        this.alumnoDAO = new AlumnoDAO();
    }

    public Computadora buscarPCPorIP(String ip) throws PersistenciaException {
        return computadoraDAO.obtenerPCPorIP(ip);
    }

    public ReservaDTO buscarReservaActiva(int idComputadora) {
        return reservaDAO.obtenerReservaActiva(idComputadora);
    }
    
    public boolean autenticarAlumno(int idAlumno, String contrasena) {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            return false; 
        }
        
        return alumnoDAO.validarCredenciales(idAlumno, contrasena);
    }
}
