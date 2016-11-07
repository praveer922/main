package seedu.testplanner.testutil;

import seedu.dailyplanner.commons.exceptions.IllegalValueException;
import seedu.dailyplanner.model.AddressBook;
import seedu.dailyplanner.model.tag.Tag;
import seedu.dailyplanner.model.task.Task;
import seedu.dailyplanner.model.task.UniqueTaskList;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withPerson("John", "Doe").withTag("Friend").build();}
 */
public class DailyPlannerBuilder {

    private AddressBook addressBook;

    public DailyPlannerBuilder(AddressBook addressBook){
        this.addressBook = addressBook;
    }

    public DailyPlannerBuilder withPerson(Task person) throws UniqueTaskList.DuplicatePersonException {
        addressBook.addPerson(person);
        return this;
    }

    public DailyPlannerBuilder withTag(String tagName) throws IllegalValueException {
        addressBook.addTag(new Tag(tagName));
        return this;
    }

    public AddressBook build(){
        return addressBook;
    }
}