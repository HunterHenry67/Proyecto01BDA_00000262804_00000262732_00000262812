/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import Entidades.Carrera;

/**
 *
 * @author Andre
 */
public interface ICarreraDAO {
    Carrera consultarCarrera(int idCarrera) throws PersistenciaException;
}
