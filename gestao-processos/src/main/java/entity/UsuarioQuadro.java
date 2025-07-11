package entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@Table(name="usuario_quadro", uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "quadro_id"}))
public class UsuarioQuadro {
    public enum Role{
        ADMIN, EMPLOYEE
    }
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", columnDefinition = "uuid", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quadro_id", columnDefinition = "uuid", nullable = false)
    private Quadro quadro;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    public UsuarioQuadro() {
    }

    public UsuarioQuadro(Usuario usuario, Quadro quadro, Role role) {
        this.usuario = usuario;
        this.quadro = quadro;
        this.role = role;
    }

    public UsuarioQuadro(UUID id, Usuario usuario, Quadro quadro, Role role) {
        this.id = id;
        this.usuario = usuario;
        this.quadro = quadro;
        this.role = role;
    }

    public Quadro getQuadro() {
        return quadro;
    }

    public void setQuadro(Quadro quadro) {
        this.quadro = quadro;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
