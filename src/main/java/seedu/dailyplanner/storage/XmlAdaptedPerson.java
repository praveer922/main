package seedu.dailyplanner.storage;

import javax.xml.bind.annotation.XmlElement;

import seedu.dailyplanner.commons.exceptions.IllegalValueException;
import seedu.dailyplanner.model.tag.Tag;
import seedu.dailyplanner.model.tag.UniqueTagList;
import seedu.dailyplanner.model.task.*;

import java.util.ArrayList;
import java.util.List;

/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedPerson {

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String phone;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private String email;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private String isComplete;
    @XmlElement(required = true)
    private boolean isPinned;

    @XmlElement
    private List<XmlAdaptedTag> tagged = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public XmlAdaptedPerson() {
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source
     *            future changes to this will not affect the created
     *            XmlAdaptedPerson
     */
    public XmlAdaptedPerson(ReadOnlyTask source) {
	name = source.getName().fullName;
	phone = source.getPhone().value;
	endDate = source.getPhone().endDate;
	email = source.getEmail().value;
	address = source.getAddress().value;
	isComplete = source.getCompletion();
	isPinned = source.isPinned();
	tagged = new ArrayList<>();
	for (Tag tag : source.getTags()) {
	    tagged.add(new XmlAdaptedTag(tag));
	}
    }

    /**
     * Converts this jaxb-friendly adapted person object into the model's Person
     * object.
     *
     * @throws IllegalValueException
     *             if there were any data constraints violated in the adapted
     *             person
     */
    public Task toModelType() throws IllegalValueException {
	final List<Tag> personTags = new ArrayList<>();
	for (XmlAdaptedTag tag : tagged) {
	    personTags.add(tag.toModelType());
	}
	final Name name = new Name(this.name);
	final Date phone = new Date(this.phone, this.endDate);
	final StartTime email = new StartTime(this.email);
	final EndTime address = new EndTime(this.address);
	final UniqueTagList tags = new UniqueTagList(personTags);
	Task newTask = new Task(name, phone, email, address, tags, isComplete);
	return newTask;
    }
}
