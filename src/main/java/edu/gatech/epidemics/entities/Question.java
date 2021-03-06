package edu.gatech.epidemics.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "question")
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "age_group_id")
    private AgeGroup ageGroup;
    @NotNull
    private Integer questionNumber;
    @NotNull
    private String questionText;
    @NotNull
    private String domainOfControl;
    @OneToMany(mappedBy = "questionId", cascade = CascadeType.ALL)
    @OrderBy("answerNumber")
    private Set<AnswerChoice> answerChoices;

    public Question() {
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(Integer questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDomainOfControl() {
        return domainOfControl;
    }

    public void setDomainOfControl(String domainOfControl) {
        this.domainOfControl = domainOfControl;
    }

    public AgeGroup getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(AgeGroup ageGroup) {
        this.ageGroup = ageGroup;
    }

    public Set<AnswerChoice> getAnswerChoices() {
        return answerChoices;
    }

    public void setAnswerChoices(Set<AnswerChoice> answerChoices) {
        this.answerChoices = answerChoices;
    }

    @Override
    public String toString() {
        return "Question{" + "id=" + id + ", ageGroup=" + ageGroup + ", questionNumber=" + questionNumber + ", questionText=" + questionText + ", domainOfControl=" + domainOfControl + ", answerChoices=" + answerChoices + '}';
    }
}
