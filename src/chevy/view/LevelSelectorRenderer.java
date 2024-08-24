package chevy.view;

import chevy.utils.Pair;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.Component;

/**
 * Rende possibile avere elementi non selezionabili in JComboBox
 * <a href="https://stackoverflow.com/questions/23722706/how-to-disable-certain-items-in-a-jcombobox">...</a>
 */
public class LevelSelectorRenderer extends BasicComboBoxRenderer {
    private final ListSelectionModel model = new DefaultListSelectionModel();
    private Pair<Integer, Integer> enabledInterval = new Pair<>(0, 1);

    public LevelSelectorRenderer(JComboBox<?> comboBox) { comboBox.setRenderer(this); }

    public boolean isInsideInterval(final int x) { return x >= enabledInterval.first && x <= enabledInterval.second; }

    public void setEnabledInterval(Pair<Integer, Integer> interval) {
        enabledInterval = interval;
        model.setSelectionInterval(enabledInterval.first, enabledInterval.second);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (!model.isSelectedIndex(index)) { // not enabled
            if (isSelected) {
                c.setBackground(UIManager.getColor("ComboBox.background"));
            } else {
                c.setBackground(super.getBackground());
            }
            c.setForeground(UIManager.getColor("Button.disabledText"));
        } else {
            c.setBackground(super.getBackground());
            c.setForeground(super.getForeground());
        }
        return c;
    }
}