
package acme.entities.S3;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FlightLeg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Column(nullable = false)
	private String				destination;
}
