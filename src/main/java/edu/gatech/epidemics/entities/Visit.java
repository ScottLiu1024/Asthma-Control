package edu.gatech.epidemics.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "visit")
public class Visit implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String[] LEVELS = {"Well Controlled", "Not Well Controlled", "Very Poorly Controlled"};
    private static final String[][] TREATMENT = {
    {
        "<li>Maintain current treatment.</li>\n"
        + "<li>Regular followup every 1-6 months.</li>\n"
        + "<li>Consider step down if well controlled for at least 3 months.</li>",
        "<li>Step up (1 step) and</li>\n"
        + "<li>Reevaluate in 2-6 weeks.</li>\n"
        + "<li>If no clear benefit in 4-6 weeks, consider alternative diagnoses or adjusting therapy.</li>\n"
        + "<li>For side effects, consider alternative treatment options.</li>",
        "<li>Consider short course of oral systemic corticosteroids,</li>\n"
        + "<li>Step up (1-2 steps), and</li>\n"
        + "<li>Reevaluate in 2 weeks.</li>\n"
        + "<li>If no clear benefit in 4-6 weeks, consider alternative diagnoses or adjusting therapy.</li>\n"
        + "<li>For side effects, consider alternative treatment options.</li>"
    },
    {
        "<li>Maintain current step.</li>\n"
        + "<li>Regular followup every 1-6 months.</li>\n"
        + "<li>Consider step down if well controlled for at least 3 months.</li>",
        "<li>Step up at least 1 step and</li>\n"
        + "<li>Reevaluate in 2-6 weeks.</li>\n"
        + "<li>For side effects: consider alternative treatment options.</li>",
        "<li>Consider short course of oral systemic corticosteroids,</li>\n"
        + "<li>Step up 1-2 steps, and</li>\n"
        + "<li>Reevaluate in 2 weeks.</li>\n"
        + "<li>For side effects, consider alternative treatment options.</li>"
    },
    {
        "<li>Maintain current step.</li>\n"
        + "<li>Regular followups every 1-6 months to maintain control.</li>\n"
        + "<li>Consider step down if well controlled for at least 3 months.</li>",
        "<li>Step up 1 step and</li>\n"
        + "<li>Reevaluate in 2-6 weeks.</li>\n"
        + "<li>For side effects, consider alternative treatment options.</li>",
        "<li>Consider short course of oral systemic corticosteroids,</li>\n"
        + "<li>Step up 1-2 steps, and</li>\n"
        + "<li>Reevaluate in 2 weeks.</li>\n"
        + "<li>For side effects, consider alternative treatment options.</li>"
    }
    };
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    private Integer patientId;
    @NotNull
    private Date visitDate;
    @OneToMany(mappedBy = "visitId", cascade = CascadeType.ALL)
    @OrderBy("question")
    private List<Response> responses;
    @OneToMany(mappedBy = "visitId", cascade = CascadeType.ALL)
    @OrderBy("domainOfControl")
    private List<Assessment> assessments;

    public Visit() {
    }

    public Visit(@NotNull Integer patientId, @NotNull Date visitDate) {
        this.patientId = patientId;
        this.visitDate = visitDate;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Date getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Date visitDate) {
        this.visitDate = visitDate;
    }

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

    public List<Assessment> getAssessments() {
        return assessments;
    }

    public void setAssessments(List<Assessment> assessments) {
        this.assessments = assessments;
    }
    
    public void assess() {
        int ageGroupId = responses.get(0).getQuestion().getAgeGroup().getId();
        if (assessments == null) {
            assessments = new LinkedList<>();
        }
        
        int maxResponseImpairment = responses.stream()
            .filter(r -> r.getQuestion().getDomainOfControl().equals("Impairment"))
            .filter(r -> r.getAnswer().getAnswerNumber() <= 3)
            .mapToInt(r -> r.getAnswer().getAnswerNumber())
            .max().getAsInt();
        int maxResponseRisk = responses.stream()
            .filter(r -> r.getQuestion().getDomainOfControl().equals("Risk"))
            .filter(r -> r.getAnswer().getAnswerNumber() <= 3)
            .mapToInt(r -> r.getAnswer().getAnswerNumber())
            .max().getAsInt();
        Assessment assessmentImpairment = new Assessment();
        assessmentImpairment.setVisitId(id);
        assessmentImpairment.setDomainOfControl("Impairment");
        Assessment assessmentRisk = new Assessment();
        assessmentRisk.setVisitId(id);
        assessmentRisk.setDomainOfControl("Risk");
        assessmentImpairment.setTreatmentText(TREATMENT[ageGroupId-1][maxResponseImpairment-1]);
        assessmentRisk.setTreatmentText(TREATMENT[ageGroupId-1][maxResponseRisk-1]);
        assessmentImpairment.setAssessmentLevel(LEVELS[maxResponseImpairment-1]);
        assessmentRisk.setAssessmentLevel(LEVELS[maxResponseRisk-1]);
        assessments.add(assessmentImpairment);
        assessments.add(assessmentRisk);
    }

    @Override
    public String toString() {
        return "Visit{" + "id=" + id + ", patientId=" + patientId + ", visitDate=" + visitDate + ", responses=" + responses + ", assessments=" + assessments + '}';
    }
}
