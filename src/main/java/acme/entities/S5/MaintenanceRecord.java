
package acme.entities.S5;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.constraints.ValidTicker;
import acme.entities.Group.Aircraft;
import acme.realms.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MaintenanceRecord extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long		serialVersionUID	= 1L;

	// Attributess -------------------------------------------------------------	
	@Mandatory
	@ValidTicker
	@Column(unique = true)
	private String					ticker;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date					moment;

	@Mandatory
	@Enumerated(EnumType.STRING)
	@Automapped
	private MaintenanceRecordStatus	status;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date					nextInspectionDueTime;

	@Mandatory
	@ValidMoney(min = 0)
	@Automapped
	private Money					estimatedCost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String					notes;

	@Mandatory
	@Automapped
	private boolean					draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Technician				technician; //un tecnico puede realizar varios registros de mantenimiento

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Aircraft				aircraft; //una aeronave puede tener multiples registros de mantenimineto
}
