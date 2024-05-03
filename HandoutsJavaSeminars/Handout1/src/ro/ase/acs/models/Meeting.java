package ro.ase.acs.models;
import java.util.Arrays;
public class Meeting implements Cloneable {

    private Priority priority ;
    private String name;
    private int startTime = 0;
    private int endTime = 0;
    private String[] participants = null;

    public Meeting() {
        this.name = "";
        this.startTime = 0;
        this.endTime = 0;
        this.priority = Priority.low;
    }

    public Meeting(String name, int startTime, int duration) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = startTime + duration;
        this.priority = Priority.low;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setParticipants(String[] participants) {
        if (participants != null) {
            this.participants = new String[participants.length];
            for (int i = 0; i < participants.length; i++) {
                this.participants[i] = new String(participants[i]);
            }
        } else {
            this.participants = null;
        }
    }


    public String[] getParticipants() {
        if (this.participants != null) {
            String[] copy = new String[this.participants.length];
            for (int i = 0; i < this.participants.length; i++) {
                copy[i] = new String(this.participants[i]);
            }
            return copy;
        } else {
            return null;
        }
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        Meeting clonedMeeting = (Meeting) super.clone();
        if (participants != null) {
            clonedMeeting.participants = new String[participants.length];
            System.arraycopy(participants, 0, clonedMeeting.participants, 0, participants.length);
        }
        return clonedMeeting;
    }

    public boolean checkParticipant(String participant) {
        if (participants != null) {
            for (String p : participants) {
                if (p.equals(participant)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void concatenate(Meeting compared) {
        this.name += "/" + compared.name;
        this.startTime = Math.min(this.startTime, compared.startTime);
        this.endTime = Math.max(this.endTime, compared.endTime);

        if (this.priority.ordinal() < compared.priority.ordinal()) {
            this.priority = compared.priority;
        }

        if (compared.participants != null) {
            for (String participant : compared.participants) {
                if (!checkParticipant(participant)) {
                    String[] newParticipants = new String[(participants != null ? participants.length : 0) + 1];
                    if (participants != null) {
                        System.arraycopy(participants, 0, newParticipants, 0, participants.length);
                    }
                    newParticipants[newParticipants.length - 1] = participant;
                    participants = newParticipants;
                }
            }
        }
    }
}
