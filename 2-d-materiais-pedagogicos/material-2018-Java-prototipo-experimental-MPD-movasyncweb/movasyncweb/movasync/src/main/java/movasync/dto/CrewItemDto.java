package movasync.dto;

public class CrewItemDto {
    private final String department;
    private final String job;
    private final String name;
    private final int id;

    public CrewItemDto(String department, String job, String name, int id) {
        this.department = department;
        this.job = job;
        this.name = name;
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public int getId() {
        return id;
    }
}
