package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.session.SessionEntry;

/**
 * Panel containing the list of sessions for the currently selected pet.
 */
public class SessionListPanel extends UiPart<Region> {

    private static final String FXML = "SessionListPanel.fxml";

    @FXML
    private Label sessionPanelTitle;

    @FXML
    private ListView<SessionEntry> sessionListView;

    /**
     * Creates a {@code SessionListPanel} bound to the given observable session list.
     * The panel updates automatically whenever the list changes.
     */
    public SessionListPanel(ObservableList<SessionEntry> sessionList) {
        super(FXML);
        sessionListView.setItems(sessionList);
        sessionListView.setCellFactory(listView -> new SessionListViewCell());
        Label placeholder = new Label("No sessions to display.\nUse addsession to add a session.");
        placeholder.getStyleClass().add("cell_small_label");
        sessionListView.setPlaceholder(placeholder);
    }

    /**
     * Updates the title shown at the top of the session panel.
     */
    public void setTitle(String title) {
        sessionPanelTitle.setText(title);
    }

    /**
     * Custom {@code ListCell} that displays a {@code Session} using a {@code SessionCard}.
     */
    class SessionListViewCell extends ListCell<SessionEntry> {
        SessionListViewCell() {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(SessionEntry entry, boolean empty) {
            super.updateItem(entry, empty);
            if (empty || entry == null) {
                setGraphic(null);
                setText(null);
            } else {
                VBox section = new VBox();
                section.setFillWidth(true);
                section.setMaxWidth(Double.MAX_VALUE);
                if (entry.sessionIndex() == 1) {
                    Label sectionHeader = new Label(String.format(
                            "%s's %s (Owner #%d, Pet #%d)",
                            entry.ownerName(), entry.petName(), entry.ownerIndex(), entry.petIndex()));
                    sectionHeader.getStyleClass().add("session-section-header");
                    sectionHeader.setWrapText(true);
                    VBox.setMargin(sectionHeader, new Insets(6, 0, 4, 10));
                    section.getChildren().add(sectionHeader);
                }
                Region sessionCardRoot = (Region) new SessionCard(entry, entry.sessionIndex()).getRoot();
                sessionCardRoot.setMaxWidth(Double.MAX_VALUE);
                section.getChildren().add(sessionCardRoot);
                setGraphic(section);
            }
        }

        @Override
        protected double computePrefHeight(double width) {
            if (getGraphic() instanceof Region sectionRoot) {
                double availableWidth = Math.max(0, width - snappedLeftInset() - snappedRightInset());
                sectionRoot.setPrefWidth(availableWidth);
                sectionRoot.setMaxWidth(availableWidth);
                return snappedTopInset() + sectionRoot.prefHeight(availableWidth) + snappedBottomInset();
            }
            return super.computePrefHeight(width);
        }

        @Override
        protected void layoutChildren() {
            if (getGraphic() instanceof Region sectionRoot) {
                double availableWidth = Math.max(0, getWidth() - snappedLeftInset() - snappedRightInset());
                sectionRoot.setPrefWidth(availableWidth);
                sectionRoot.setMaxWidth(availableWidth);
            }
            super.layoutChildren();
        }
    }
}
