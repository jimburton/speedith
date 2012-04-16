/*
 * File name: GoalsTopComponent.java
 *    Author: matej
 * 
 *  Copyright © 2012 Matej Urbas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package diabelli.ui;

import diabelli.Diabelli;
import diabelli.GoalsManager;
import diabelli.logic.Formula;
import diabelli.logic.Goal;
import diabelli.logic.Goals;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.TreeTableView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//diabelli.ui//Goals//EN",
autostore = false)
@TopComponent.Description(preferredID = "GoalsTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = true)
@ActionID(category = "Window", id = "diabelli.ui.GoalsTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_GoalsAction",
preferredID = "GoalsTopComponent")
@Messages({
    "CTL_GoalsAction=Diabelli Goals",
    "CTL_GoalsTopComponent=Diabelli Goals",
    "HINT_GoalsTopComponent=This window displays the list of current Diabelli goals."
})
public final class GoalsTopComponent extends TopComponent implements ExplorerManager.Provider {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private ExplorerManager em;
    private Lookup lookup;
    private GoalsChangedListenerImpl goalsChangedListener;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public GoalsTopComponent() {
        initComponents();
        setName(Bundle.CTL_GoalsTopComponent());
        setToolTipText(Bundle.HINT_GoalsTopComponent());

        this.em = new ExplorerManager();
        ActionMap map = this.getActionMap();
        InputMap keys = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.lookup = ExplorerUtils.createLookup(this.em, map);
        this.associateLookup(this.lookup);

        // Make the root node invisible in the view:
        ((TreeTableView) goalsView).setRootVisible(false);

        updateGoalsList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goalsView = new TreeTableView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane goalsView;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TopComponent-Specific Stuff">
    @Override
    public void componentOpened() {
        // Register to 'goal change' events in Diabelli's goal manager:
        if (goalsChangedListener == null) {
            goalsChangedListener = new GoalsChangedListenerImpl();
        }
        GoalsManager goalManager = Lookup.getDefault().lookup(Diabelli.class).getGoalManager();
        goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
        updateGoalsList();
    }

    @Override
    public void componentClosed() {
        // Unregister to 'goal change' events in Diabelli's goal manager:
        if (goalsChangedListener != null) {
            GoalsManager goalManager = Lookup.getDefault().lookup(Diabelli.class).getGoalManager();
            goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Goal Explorer Nodes">
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }

    //<editor-fold defaultstate="collapsed" desc="Children Factories">
    private static class GoalChildrenFactory extends ChildFactory<GoalNode> {

        private final Goals goals;

        public GoalChildrenFactory(Goals goals) {
            this.goals = goals;
        }

        @Override
        protected boolean createKeys(List<GoalNode> toPopulate) {
            if (goals != null && !goals.isEmpty()) {
                for (int i = 0; i < goals.size(); i++) {
                    toPopulate.add(new GoalNode(goals.get(i), i));
                }
            }
            return true;
        }

        @Override
        protected GoalNode createNodeForKey(GoalNode key) {
            return key;
        }
    }

    private static class GoalPremisesConclusionFactory extends ChildFactory<AbstractNode> {

        private final Goal goal;
        private final int goalIndex;

        public GoalPremisesConclusionFactory(Goal goal, int goalIndex) {
            this.goal = goal;
            this.goalIndex = goalIndex;
        }

        @Override
        protected boolean createKeys(List<AbstractNode> toPopulate) {
            if (goal != null) {
                if (goal.getPremisesCount() > 0) {
                    toPopulate.add(new PremisesNode(goal, goalIndex));
                }
                if (goal.getConclusion() != null) {
                    toPopulate.add(new ConclusionNode(goal, goalIndex));
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(AbstractNode key) {
            return key;
        }
    }

    private static class PremisesFactory extends ChildFactory<AbstractNode> {

        private final Goal goal;
        private final int goalIndex;

        public PremisesFactory(Goal goal, int goalIndex) {
            this.goal = goal;
            this.goalIndex = goalIndex;
        }

        @Override
        protected boolean createKeys(List<AbstractNode> toPopulate) {
            int premisesCount = goal.getPremisesCount();
            for (int premiseIndex = 0; premiseIndex < premisesCount; premiseIndex++) {
                toPopulate.add(new PremiseNode(goal, goalIndex, premiseIndex));
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(AbstractNode key) {
            return key;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Explorer Nodes">
    public abstract static class GeneralGoalNode extends AbstractNode {
        public final Goal goal;
        public final int goalIndex;
        
        public GeneralGoalNode(Goal goal, int goalIndex, Children children, Lookup lookup) {
            super(children, lookup);
            this.goal = goal;
            this.goalIndex = goalIndex;
        }

        public GeneralGoalNode(Goal goal, int goalIndex, Children children) {
            super(children);
            this.goal = goal;
            this.goalIndex = goalIndex;
        }
        
    }
    
    public static class GoalNode extends GeneralGoalNode {

        private GoalNode(Goal goal, int goalIndex) {
            super(goal, goalIndex, Children.create(new GoalPremisesConclusionFactory(goal, goalIndex), false), Lookups.singleton(goal));
            setDisplayName("Goal #" + (goalIndex + 1));
        }
    }

    @Messages({
        "FN_conclusion_display_name=Conclusion"
    })
    public static class ConclusionNode extends GeneralGoalNode {

        private ConclusionNode(Goal parentGoal, int goalIndex) {
            super(parentGoal, goalIndex, Children.LEAF);
            setDisplayName(Bundle.FN_conclusion_display_name());
        }
    }

    @Messages({
        "PN_premises_display_name=Premises"
    })
    public static class PremisesNode extends GeneralGoalNode {

        private PremisesNode(Goal parentGoal, int goalIndex) {
            super(parentGoal, goalIndex, Children.create(new PremisesFactory(parentGoal, goalIndex), false));
            setDisplayName(Bundle.PN_premises_display_name());
        }
    }

    @Messages({
        "PN_premise_display_name=Premise #{0}"
    })
    public static class PremiseNode extends GeneralGoalNode {

        public final int premiseIndex;

        private PremiseNode(Goal goal, int goalIndex, int premiseIndex){
            super(goal, goalIndex, Children.LEAF);
            this.premiseIndex = premiseIndex;
            this.setDisplayName(Bundle.PN_premise_display_name(premiseIndex + 1));
        }
    }
    //</editor-fold>
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event Handlers">
    private class GoalsChangedListenerImpl implements PropertyChangeListener {

        private GoalsChangedListenerImpl() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateGoalsList((Goals) evt.getNewValue());
        }
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="UI Refresh Methods">
    private void updateGoalsList(Goals goals) {
        Children children = Children.create(new GoalChildrenFactory(goals), false);
        Node root = new AbstractNode(children);
        this.em.setRootContext(root);
        this.em.getRootContext().setDisplayName("Diabelli Goals List");
    }

    private void updateGoalsList() {
        GoalsManager goalManager = Lookup.getDefault().lookup(Diabelli.class).getGoalManager();
        if (goalManager.getCurrentGoals() != null) {
            updateGoalsList(goalManager.getCurrentGoals());
        } else {
            updateGoalsList(null);
        }
    }
    // </editor-fold>
}
