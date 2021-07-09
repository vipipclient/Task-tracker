package com.berdibekov.writer.console_writer;


import com.berdibekov.domain.Project;
import com.berdibekov.domain.Report;
import com.berdibekov.domain.Task;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.asciithemes.TA_GridThemes;
import de.vandermeer.asciithemes.a7.A7_Grids;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.springframework.stereotype.Component;

@Component
public class ReportPrinter {

    static final String GREEN = "\u001B[32m";
    static final String RESET_BLACK = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String YELLOW = "\u001B[33m";

    public void writeHeader() {
        AsciiTable at = new AsciiTable();
        at.addRule();
        AT_Row row = at.addRow("Task id", "Parent task id", "project", "Status", "Name", "Description");
        setTableOptions(at, row);
        String table = at.render();
        System.out.println(table);
    }

    void writeReportHeader(Report report) {
        AsciiTable at = new AsciiTable();
        at.addRule();
        at.addRule();
        at.addRow("User name : " + report.getUser().getFirstName());
        at.addRow("ProjectName : " + report.getProject().getName());
        at.addRule();
        at.addRule();
        at.getContext().setWidth(155).setGridTheme(TA_GridThemes.OUTSIDE).setGrid(A7_Grids.minusBarPlusEquals());
        at.setTextAlignment(TextAlignment.CENTER);
        System.out.println(at.render());
    }

    public void printTasks(Iterable<Task> tasks) {
        AsciiTable at = new AsciiTable();
        for (Task task : tasks) {
            String taskId = task.getParentTask() != null ? Long.toString(task.getParentTask().getId()) : "-";
            at.addRule();
            Project project = task.getProject();
            AT_Row row = at.addRow(task.getId(), taskId, "№ "+ project.getId() + ". " + project.getName(), task.getStatus(),
                                   task.getName(), prepareDescription(task.getDescription()));
            setTableOptions(at, row);
        }
        at.addRule();
        System.out.println(at.render());
    }

    private void setTableOptions(AsciiTable at, AT_Row row) {
        CWC_FixedWidth cwc = new CWC_FixedWidth();
        cwc.add(11).add(11).add(20).add(11).add(30).add(100);
        at.getRenderer().setCWC(cwc);
        at.getContext().setGrid(A7_Grids.minusBarPlusEquals());
        row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(2).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(3).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(4).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(5).getContext().setTextAlignment(TextAlignment.LEFT);
    }

    /**
     * @param description
     * @return edited description to be used for printing with {@link AsciiTable}
     */
    private String prepareDescription(String description) {
        return description.replace("\n", "<br> <br/>");
    }
}
