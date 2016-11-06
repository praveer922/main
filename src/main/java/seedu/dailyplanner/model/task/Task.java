package seedu.dailyplanner.model.task;

import java.util.Objects;

import seedu.dailyplanner.commons.util.CollectionUtil;
import seedu.dailyplanner.commons.util.DateUtil;
import seedu.dailyplanner.commons.util.StringUtil;
import seedu.dailyplanner.model.tag.UniqueTagList;

/**
 * Represents a Person in the address book. Guarantees: details are present and
 * not null, field values are validated.
 */
public class Task implements ReadOnlyTask, Comparable<Task> {

	private String taskName;
	private DateTime start;
	private DateTime end;
	private boolean isComplete;
	private boolean isPinned;
	private UniqueTagList tags;

	/**
	 * Every field must be present and not null.
	 */
	public Task(String name, DateTime start, DateTime end, boolean isComplete, boolean isPinned, UniqueTagList tags) {
		assert !CollectionUtil.isAnyNull(name, start, end, isComplete, isPinned, tags);
		this.taskName = name;
		this.start = start;
		this.end = end;
		this.tags = new UniqueTagList(tags); // protect internal tags from
		// changes in the arg list
		this.isComplete = isComplete;
		this.isPinned = isPinned;
	}


    /**
	 * Copy constructor.
	 */
	public Task(ReadOnlyTask source) {
		this(source.getName(), source.getStart(), source.getEnd(), source.isComplete(), source.isPinned(),
				source.getTags());
	}

	@Override
	public void setName(String name) {
		this.taskName = name;
	}

	@Override
	public void setStart(DateTime startDate) {
		this.start = startDate;
	}

	@Override
	public void setEnd(DateTime endDate) {
		this.end = endDate;
	}

	@Override
	public void markAsComplete() {
		this.isComplete = true;
	}

	@Override
	public void markAsNotComplete() {
		this.isComplete = false;
	}

	public void pin() {
		this.isPinned = true;
	}

	public void unpin() {
		this.isPinned = false;
	}
	
	private String calculateDueStatus(DateTime end) {
	    if(end.getTime().toString().equals("")) {
	        return "";
	        
	    }
	    DateTime nowAsDateTime = DateUtil.nowAsDateTime();
	    if (!DateUtil.checkDatePrecedence(end, nowAsDateTime) && (!end.getDate().equals(nowAsDateTime.getDate()))) {
	        return "OVERDUE";
	    }
	    else if (end.getDate().equals(nowAsDateTime.getDate())) {
         int overDueHours = end.getTime().m_hour - nowAsDateTime.getTime().m_hour;
         if (overDueHours<0) {
             return "OVERDUE";
         } else if (overDueHours ==0) {
             if(DateUtil.checkTimePrecendence(end, nowAsDateTime)) {
                 return "OVERDUE";
             } else {
                 return "DUE SOON";
             }
         }
         else if (overDueHours>=0 && overDueHours<=3) {
             return "DUE SOON";
         }
        }
        return "";
	}

	@Override
	public String getName() {
		return taskName;
	}

	@Override
	public DateTime getStart() {
		return start;
	}

	@Override
	public DateTime getEnd() {
		return end;
	}

	@Override
	public String getCompletion() {
		return (isComplete) ? "COMPLETE" : "NOT COMPLETE";
	}
	
	 @Override
	    public String getDueStatus() {
	        return calculateDueStatus(end);
	    }

	@Override
	public boolean isComplete() {
		return isComplete;
	}

	public boolean isPinned() {
		return isPinned;
	}

	@Override
	public UniqueTagList getTags() {
		return new UniqueTagList(tags);
	}

	/**
	 * Replaces this task's tags with the tags in the argument tag list.
	 */
	public void setTags(UniqueTagList replacement) {
		tags.setTags(replacement);
	}

	@Override
	public boolean equals(Object other) {
		return other == this // short circuit if same object
				|| (other instanceof ReadOnlyTask // instanceof handles nulls
						&& this.isSameStateAs((ReadOnlyTask) other));
	}

	@Override
	public int hashCode() {
		return Objects.hash(taskName, start, end, isComplete, isPinned, tags);
	}

	@Override
	public String toString() {
		return getAsText();
	}

	@Override
	public int compareTo(Task o) {
		if (!start.equals(o.start)) {
			return start.compareTo(o.start);
		} else if (!end.equals(o.end)) {
			return end.compareTo(o.end);
		} else
			return taskName.compareTo(o.taskName);
	}


   

}
