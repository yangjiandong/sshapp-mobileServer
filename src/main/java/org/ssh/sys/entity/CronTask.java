package org.ssh.sys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

//自动任务
@Entity
@Table(name = "T_CronTask")
public class CronTask implements Serializable {

	private static final long serialVersionUID = 2136790018031187866L;

	private Long id;
	private String taskName;
	private String cronExpression;
	private String note;

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Column(nullable = false, unique = true, length = 100)
	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Column(length = 100)
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	@Column(length = 250)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
