/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Dtos.ObtenerCatalogoSoftwareDTO;
import Entidades.Software;
import java.util.List;

/**
 *
 * @author Andre
 */
public interface ISoftwareDAO {
    List<Software> obtenerCatalogoSoftware(ObtenerCatalogoSoftwareDTO obtenerCatalogo) throws PersistenciaException;
}
