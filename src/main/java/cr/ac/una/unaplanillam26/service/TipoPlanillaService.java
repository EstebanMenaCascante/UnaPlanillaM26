package cr.ac.una.unaplanillam26.service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import cr.ac.una.unaplanillam26.model.Empleado;
import cr.ac.una.unaplanillam26.model.EmpleadoDto;
import cr.ac.una.unaplanillam26.model.TipoPlanilla;
import cr.ac.una.unaplanillam26.model.TiposPlanillaDto;
import cr.ac.una.unaplanillam26.util.EntityManagerHelper;
import cr.ac.una.unaplanillam26.util.Respuesta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

/**
 *
 * @author emena
 */
public class TipoPlanillaService {

    private final EntityManager em = EntityManagerHelper.getManager();
    private EntityTransaction et;

    public Respuesta buscarTipoPlanillas(String codigo, String descripcion, String nombreEmpleado) {
        try {
            em.clear();

            StringBuilder jpql = new StringBuilder("SELECT DISTINCT t FROM TipoPlanilla t");
            boolean filtrarPorNombre = nombreEmpleado != null && !nombreEmpleado.isBlank();

            if (filtrarPorNombre) {
                jpql.append(" JOIN t.empleados e");
            }

            jpql.append(" WHERE 1=1");

            if (codigo != null && !codigo.isBlank()) {
                jpql.append(" AND UPPER(t.codigo) LIKE :codigo");
            }

            if (descripcion != null && !descripcion.isBlank()) {
                jpql.append(" AND UPPER(t.descripcion) LIKE :descripcion");
            }

            if (filtrarPorNombre) {
                jpql.append(" AND UPPER(e.nombre) LIKE :nombreEmpleado");
            }

            TypedQuery<TipoPlanilla> qryTipoPlanilla = em.createQuery(jpql.toString(), TipoPlanilla.class);

            if (codigo != null && !codigo.isBlank()) {
                qryTipoPlanilla.setParameter("codigo", "%" + codigo.trim().toUpperCase() + "%");
            }

            if (descripcion != null && !descripcion.isBlank()) {
                qryTipoPlanilla.setParameter("descripcion", "%" + descripcion.trim().toUpperCase() + "%");
            }

            if (filtrarPorNombre) {
                String nombreEmpleadoValor = nombreEmpleado == null ? "" : nombreEmpleado.trim().toUpperCase();
                qryTipoPlanilla.setParameter("nombreEmpleado", "%" + nombreEmpleadoValor + "%");
            }

                List<TiposPlanillaDto> tiposPlanilla = qryTipoPlanilla.getResultStream()
                    .map(TiposPlanillaDto::new)
                    .collect(Collectors.toList());

            return new Respuesta(true, "", "", "TipoPlanillas", tiposPlanilla);
        } catch (Exception ex) {
            Logger.getLogger(TipoPlanillaService.class.getName()).log(Level.SEVERE, "Error consultando planillas", ex);
            return new Respuesta(false, "Error consultando las planillas.", "buscarTipoPlanillas " + ex.getMessage());
        }
    }

   public Respuesta getTipoPlanilla(Long id) {
        try {
            em.clear();
            TypedQuery<TipoPlanilla> qryTipoPlanilla = em.createNamedQuery("TipoPlanilla.findByTplaId", TipoPlanilla.class);
            qryTipoPlanilla.setParameter("id", id);
            TipoPlanilla tipoPlanilla = qryTipoPlanilla.getSingleResult();
            tipoPlanilla.getEmpleados().size();

            TiposPlanillaDto tipoPlanillaDto = new TiposPlanillaDto(tipoPlanilla);
            return new Respuesta(true, "", "", "TipoPlanilla", tipoPlanillaDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un tipo de planilla con el id ingresado.", "getTipoPlanilla NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(TipoPlanillaService.class.getName()).log(Level.SEVERE, "Ocurrio un error al consultar el tipo de planilla.", ex);
            return new Respuesta(false, "Ocurrio un error al consultar el tipo de planilla.", "getTipoPlanilla NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(TipoPlanillaService.class.getName()).log(Level.SEVERE, "Error obteniendo el tipo de planilla [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el tipo de planilla.", "getTipoPlanilla " + ex.getMessage());
        }
    }

    public Respuesta guardarTipoPlanilla(TiposPlanillaDto tipoPlanillaDto) {
        try {
            et = em.getTransaction();
            et.begin();

            TipoPlanilla tipoPlanilla;
            if (tipoPlanillaDto.getId() != null && tipoPlanillaDto.getId() > 0) {
                em.clear();
                tipoPlanilla = em.find(TipoPlanilla.class, tipoPlanillaDto.getId());
                if (tipoPlanilla == null) {
                    return new Respuesta(false, "No se encontro el tipo de planilla a modificar.", "guardarTipoPlanilla NoResultException");
                }
                tipoPlanilla.actualizar(tipoPlanillaDto);
                
                
           for (EmpleadoDto empleadoEliminado : tipoPlanillaDto.getEmpleadosEliminados()) {
                tipoPlanilla.getEmpleados().removeIf(e -> e.getId().equals(empleadoEliminado.getId()));
            }

            if (!tipoPlanillaDto.getEmpleados().isEmpty()) {
                for (EmpleadoDto empleadoDto : tipoPlanillaDto.getEmpleados()) {
                    if (empleadoDto.getModificado()){
                        Empleado empleado = em.find(Empleado.class, empleadoDto.getId());
                        empleado.getTiposPlanilla().add(tipoPlanilla);
                        tipoPlanilla.getEmpleados().add(empleado);
                    }
                }
            }

                tipoPlanilla = em.merge(tipoPlanilla);
            } else {
                tipoPlanilla = new TipoPlanilla(tipoPlanillaDto);
                em.persist(tipoPlanilla);
            }
            et.commit();
            return new Respuesta(true, "", "", "TipoPlanilla", new TiposPlanillaDto(tipoPlanilla));
        } catch (Exception ex) {
            Logger.getLogger(TipoPlanillaService.class.getName()).log(Level.SEVERE, "Error guardando el tipo de planilla", ex);
            if (et != null && et.isActive()) {
                et.rollback();
            }
            return new Respuesta(false, "Error guardando el tipo de planilla.", "guardarTipoPlanilla " + ex.getMessage());
        }
    }

    public Respuesta eliminarTipoPlanilla(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            em.clear();

            TipoPlanilla tipoPlanilla;
            if (id != null && id > 0) {
                tipoPlanilla = em.find(TipoPlanilla.class, id);
                if (tipoPlanilla == null) {
                    return new Respuesta(false, "No se encontro el tipo de planilla a eliminar.", "eliminarTipoPlanilla NoResultException");
                }
                em.remove(tipoPlanilla);
            } else {
                return new Respuesta(false, "Favor consultar el tipo de planilla a eliminar", "");
            }
            et.commit();
            return new Respuesta(true, "", "", "TipoPlanilla", new TiposPlanillaDto(tipoPlanilla));
        } catch (Exception ex) {
            Logger.getLogger(TipoPlanillaService.class.getName()).log(Level.SEVERE, "Error eliminando el tipo de planilla", ex);
            if (et != null && et.isActive()) {
                et.rollback();
            }
            return new Respuesta(false, "Error eliminando el tipo de planilla.", "eliminarTipoPlanilla " + ex.getMessage());
        }
    }
}