/*
 * PersonViewPanel
 *
 * Created on July 26, 2009, 11:32 PM
 */

package mekhq.gui.view;

import java.awt.*;
import java.awt.Dialog.ModalityType;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;

import megamek.common.Crew;
import megamek.common.options.PilotOptions;
import megamek.common.util.DirectoryItems;
import megamek.common.util.EncodeControl;
import mekhq.IconPackage;
import mekhq.MekHQ;
import mekhq.campaign.personnel.Award;
import mekhq.campaign.Campaign;
import mekhq.campaign.Kill;
import mekhq.campaign.log.LogEntry;
import mekhq.campaign.event.PersonChangedEvent;
import mekhq.campaign.personnel.Injury;
import mekhq.campaign.personnel.Person;
import mekhq.campaign.personnel.SkillType;
import mekhq.gui.dialog.MedicalViewDialog;
import mekhq.gui.model.PersonnelEventLogModel;
import mekhq.gui.model.PersonnelKillLogModel;
import mekhq.gui.utilities.ImageHelpers;
import mekhq.gui.utilities.WrapLayout;

/**
 * A custom panel that gets filled in with goodies from a Person record
 * @author  Jay Lawson <jaylawson39 at yahoo.com>
 */
public class PersonViewPanel extends ScrollablePanel {
    private static final long serialVersionUID = 7004741688464105277L;

    private static final int MAX_NUMBER_OF_RIBBON_AWARDS_PER_ROW = 4;

    private Person person;
    private Campaign campaign;

    private DirectoryItems portraits;
    private DirectoryItems awardIcons;
    private IconPackage ip;

    private JPanel pnlPortrait;
    private JLabel lblPortrait;
    private JPanel pnlInfo;
    private JPanel pnlSkills;
    private JPanel pnlFamily;

    private JTextArea txtDesc;
    private JPanel pnlKills;
    private JPanel pnlLog;
    private JPanel pnlMissionsLog;
    private JPanel pnlInjuries;

    private JLabel lblType;
    private JLabel lblCall1;
    private JLabel lblCall2;
    private JLabel lblAge1;
    private JLabel lblAge2;
    private JLabel lblGender1;
    private JLabel lblGender2;
    private JLabel lblStatus1;
    private JLabel lblStatus2;
    private JLabel lblOrigin1;
    private JLabel lblOrigin2;
    private JLabel lblRecruited1;
    private JLabel lblRecruited2;
    private JLabel lblTimeServed1;
    private JLabel lblTimeServed2;
    private JLabel lblDuedate1;
    private JLabel lblDuedate2;
    private JLabel lblTough1;
    private JLabel lblTough2;
    private JLabel lblEdge1;
    private JLabel lblEdge2;
    private JLabel lblEdgeAvail1;
    private JLabel lblEdgeAvail2;
    private JLabel lblAbility1;
    private JLabel lblAbility2;
    private JLabel lblImplants1;
    private JLabel lblImplants2;
    private JLabel lblAdvancedMedical1;
    private JLabel lblAdvancedMedical2;
    private JLabel lblSpouse1;
    private JLabel lblSpouse2;
    private JLabel lblChildren1;
    private JLabel lblChildren2;
    private JPanel pnlAllAwards;
    private JPanel pnlMedals;
    private JPanel pnlMiscAwards;
    private Box boxRibbons;

    ResourceBundle resourceMap = null;


    public PersonViewPanel(Person p, Campaign c, IconPackage ip) {
        this.person = p;
        this.campaign = c;
        this.ip = ip;
        this.portraits = ip.getPortraits();
        this.awardIcons = ip.getAwardIcons();
        resourceMap = ResourceBundle.getBundle("mekhq.resources.PersonViewPanel", new EncodeControl()); //$NON-NLS-1$
        initComponents();
    }

    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        lblPortrait = new JLabel();
        pnlPortrait = new JPanel();
        pnlKills = new JPanel();
        pnlLog = new JPanel();
        txtDesc = new JTextArea();
        pnlMissionsLog = new JPanel();
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        // Panel portrait will include the person picture and the ribbons
        pnlPortrait.setName("pnlPortrait");
        pnlPortrait.setBackground(Color.WHITE);
        pnlPortrait.setLayout(new GridBagLayout());

        GridBagConstraints gbc_pnlPortrait = new GridBagConstraints();
        gbc_pnlPortrait = new GridBagConstraints();
        gbc_pnlPortrait.gridx = 0;
        gbc_pnlPortrait.gridy = 0;
        gbc_pnlPortrait.fill = GridBagConstraints.NONE;
        gbc_pnlPortrait.anchor = GridBagConstraints.NORTHWEST;
        gbc_pnlPortrait.insets = new Insets(10,10,0,0);
        add(pnlPortrait, gbc_pnlPortrait);

        lblPortrait.setName("lblPortait"); // NOI18N
        lblPortrait.setBackground(Color.WHITE);
        setPortrait();
        
        GridBagConstraints gbc_lblPortrait = new GridBagConstraints();
        gbc_lblPortrait.gridx = 0;
        gbc_lblPortrait.gridy = 0;
        gbc_lblPortrait.fill = GridBagConstraints.NONE;
        gbc_lblPortrait.anchor = GridBagConstraints.NORTHWEST;
        gbc_lblPortrait.insets = new Insets(0,0,0,0);
        pnlPortrait.add(lblPortrait, gbc_lblPortrait);
        
        fillInfo();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(pnlInfo, gridBagConstraints);

        int gridy = 1;
        
        fillSkills();
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(pnlSkills, gridBagConstraints);
        gridy++;

        if(campaign.getCampaignOptions().useAdvancedMedical()) {
            fillInjuries();
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = gridy;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            add(pnlInjuries, gridBagConstraints);
            gridy++;
        }

        if(person.hasAnyFamily()) {
	        fillFamily();
	        gridBagConstraints = new GridBagConstraints();
	        gridBagConstraints.gridx = 0;
	        gridBagConstraints.gridy = gridy;
	        gridBagConstraints.gridwidth = 2;
	        gridBagConstraints.weightx = 1.0;
	        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
	        gridBagConstraints.fill = GridBagConstraints.BOTH;
	        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	        add(pnlFamily, gridBagConstraints);
	        gridy++;
        }

        if(person.awardController.hasAwards()) {
            if(person.awardController.hasAwardsWithRibbons()){
                boxRibbons = Box.createVerticalBox();
                boxRibbons.add(Box.createRigidArea(new Dimension(100,0)));
                drawRibbons();

                GridBagConstraints gbc_pnlAllRibbons = new GridBagConstraints();
                gbc_pnlAllRibbons.gridx = 0;
                gbc_pnlAllRibbons.gridy = 1;
                gbc_pnlAllRibbons.fill = GridBagConstraints.NONE;
                gbc_pnlAllRibbons.anchor = GridBagConstraints.NORTHWEST;
                gbc_pnlAllRibbons.insets = new Insets(0,0,0,0);
                pnlPortrait.add(boxRibbons, gbc_pnlAllRibbons);
            }

            pnlAllAwards = new JPanel();
            pnlAllAwards.setLayout(new BoxLayout(pnlAllAwards, BoxLayout.PAGE_AXIS));
            pnlAllAwards.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("pnlAwards.title")));
            pnlAllAwards.setBackground(Color.WHITE);
            
            if(person.awardController.hasAwardsWithMedals()){
                pnlMedals = new JPanel();
                pnlMedals.setName("pnlMedals");
                pnlMedals.setBackground(Color.WHITE);
                drawMedals();
                pnlAllAwards.add(pnlMedals);
                pnlMedals.setLayout(new WrapLayout(FlowLayout.LEFT));
            }

            if(person.awardController.hasAwardsWithMiscs()){
                pnlMiscAwards = new JPanel();
                pnlMiscAwards.setName("pnlMiscAwards");
                pnlMiscAwards.setBackground(Color.WHITE);
                drawMiscAwards();
                pnlAllAwards.add(pnlMiscAwards);
                pnlMiscAwards.setLayout(new WrapLayout(FlowLayout.LEFT));
            }
            
            if(person.awardController.hasAwardsWithMedals() || person.awardController.hasAwardsWithMiscs()) {
	            gridBagConstraints = new GridBagConstraints();
	            gridBagConstraints.fill = GridBagConstraints.BOTH;
	            gridBagConstraints.gridwidth = 2;
	            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
	            gridBagConstraints.gridx = 0;
	            gridBagConstraints.gridy = gridy;
	            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
	            add(pnlAllAwards, gridBagConstraints);
	            gridy++;
            }
        }
        
        if(person.getBiography().length() > 0) {
            txtDesc = new JTextArea();
            txtDesc.setName("txtDesc"); //$NON-NLS-1$
            txtDesc.setBackground(Color.WHITE);
            txtDesc.setEditable(false);
            txtDesc.setLineWrap(true);
            txtDesc.setWrapStyleWord(true);
            txtDesc.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(resourceMap.getString("pnlDescription.title")), //$NON-NLS-1$
                    BorderFactory.createEmptyBorder(5,5,5,5)));
            txtDesc.setText(person.getBiography());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = gridy;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            add(txtDesc, gridBagConstraints);
            gridy++;
        }

        if(person.getPersonnelLog().size() >0) {
            pnlLog.setName("pnlLog"); //$NON-NLS-1$
            pnlLog.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("pnlLog.title"))); //$NON-NLS-1$
            pnlLog.setBackground(Color.WHITE);
            fillLog();
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = gridy;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            add(pnlLog, gridBagConstraints);
            gridy++;
        }

        if(person.getMissionLog().size() >0) {
            fillMissionLog();

            pnlMissionsLog.setName("missionLog"); //$NON-NLS-1$
            pnlMissionsLog.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(resourceMap.getString("missionLog.title")), //$NON-NLS-1$
                    BorderFactory.createEmptyBorder(5,5,5,5)));
            pnlMissionsLog.setBackground(Color.WHITE);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = gridy;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            add(pnlMissionsLog, gridBagConstraints);
            gridy++;
        }

        if(!campaign.getKillsFor(person.getId()).isEmpty()) {
            fillKillRecord();

            pnlKills.setName("txtKills"); //$NON-NLS-1$
            pnlKills.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(resourceMap.getString("pnlKills.title")), //$NON-NLS-1$
                    BorderFactory.createEmptyBorder(5,5,5,5)));
            gridBagConstraints = new GridBagConstraints();
            pnlKills.setBackground(Color.WHITE);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = gridy;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            add(pnlKills, gridBagConstraints);
            gridy++;
        }

        //use glue to fill up the remaining space so everything is aligned to the top
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = gridy;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        add(javax.swing.Box.createGlue(), gridBagConstraints);
    }

    /**
     * Draws the ribbons below the person portrait.
     */
    private void drawRibbons() {
        List<Award> awards = person.awardController.getAwards().stream().filter(a -> a.getNumberOfRibbonFiles() > 0).sorted()
                .collect(Collectors.toList());
        Collections.reverse(awards);

        int i = 0;
        Box rowRibbonsBox = null;
        ArrayList<Box> rowRibbonsBoxes = new ArrayList<>();

        for(Award award : awards){
            JLabel ribbonLabel = new JLabel();
            Image ribbon;

            if(i%MAX_NUMBER_OF_RIBBON_AWARDS_PER_ROW == 0){
                rowRibbonsBox = Box.createHorizontalBox();
                rowRibbonsBox.setBackground(Color.RED);
            }
            try{
                int numberOfAwards = person.awardController.getNumberOfAwards(award);
                String ribbonFileName = award.getRibbonFileName(numberOfAwards);
                ribbon = (Image) awardIcons.getItem(award.getSet() + "/ribbons/", ribbonFileName);
                if(ribbon == null) continue;
                ribbon = ribbon.getScaledInstance(25,8, Image.SCALE_DEFAULT);
                ribbonLabel.setIcon(new ImageIcon(ribbon));
                ribbonLabel.setToolTipText(award.getTooltip());
                rowRibbonsBox.add(ribbonLabel, 0);
            }
            catch (Exception err) {
                err.printStackTrace();
            }

            i++;
            if(i%MAX_NUMBER_OF_RIBBON_AWARDS_PER_ROW == 0){
                rowRibbonsBoxes.add(rowRibbonsBox);
            }
        }
        if(i%MAX_NUMBER_OF_RIBBON_AWARDS_PER_ROW!=0){
            rowRibbonsBoxes.add(rowRibbonsBox);
        }

        Collections.reverse(rowRibbonsBoxes);
        for(Box box : rowRibbonsBoxes){
            boxRibbons.add(box);
        }
    }

    /**
     * Draws the medals above the personal log.
     */
    private void drawMedals(){
        List<Award> awards = person.awardController.getAwards().stream().filter(a -> a.getNumberOfMedalFiles() > 0).sorted()
                .collect(Collectors.toList());

        for(Award award : awards){
            JLabel medalLabel = new JLabel();

            Image medal = null;
            try{
                int numberOfAwards = person.awardController.getNumberOfAwards(award);
                String medalFileName = award.getMedalFileName(numberOfAwards);
                medal = (Image) awardIcons.getItem(award.getSet() + "/medals/", medalFileName);
                if(medal == null) continue;
                medal = ImageHelpers.getScaledForBoundaries(medal, new Dimension(30,60), Image.SCALE_DEFAULT);
                medalLabel.setIcon(new ImageIcon(medal));
                medalLabel.setToolTipText(award.getTooltip());
                pnlMedals.add(medalLabel);
            }
            catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    /**
     * Draws the misc awards below the medals.
     */
    private void drawMiscAwards() {
        ArrayList<Award> awards = person.awardController.getAwards().stream().filter(a -> a.getNumberOfMiscFiles() > 0)
                .collect(Collectors.toCollection(ArrayList::new));

        for (Award award : awards) {
            JLabel miscLabel = new JLabel();

            Image miscAward = null;
            try {
                int numberOfAwards = person.awardController.getNumberOfAwards(award);
                String miscFileName = award.getMiscFileName(numberOfAwards);
                Image miscAwardBufferedImage = (Image) awardIcons.getItem(award.getSet() + "/misc/", miscFileName);
                if (miscAwardBufferedImage == null) continue;
                miscAward = ImageHelpers.getScaledForBoundaries(miscAwardBufferedImage, new Dimension(100,100), Image.SCALE_DEFAULT);
                miscLabel.setIcon(new ImageIcon(miscAward));
                miscLabel.setToolTipText(award.getTooltip());
                pnlMiscAwards.add(miscLabel);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    /**
     * set the portrait for the given person.
     *
     * @return The <code>Image</code> of the pilot's portrait. This value
     *         will be <code>null</code> if no portrait was selected
     *          or if there was an error loading it.
     */
    public void setPortrait() {

        String category = person.getPortraitCategory();
        String filename = person.getPortraitFileName();

        if(Crew.ROOT_PORTRAIT.equals(category)) {
            category = ""; //$NON-NLS-1$
        }

        // Return a null if the player has selected no portrait file.
        if ((null == category) || (null == filename) || Crew.PORTRAIT_NONE.equals(filename)) {
            filename = "default.gif"; //$NON-NLS-1$
        }

        // Try to get the player's portrait file.
        Image portrait = null;
        try {
            portrait = (Image) portraits.getItem(category, filename);
            if(null != portrait) {
                portrait = portrait.getScaledInstance(100, -1, Image.SCALE_DEFAULT);
            } else {
                portrait = (Image) portraits.getItem("", "default.gif");  //$NON-NLS-1$ //$NON-NLS-2$
                if(null != portrait) {
                    portrait = portrait.getScaledInstance(100, -1, Image.SCALE_DEFAULT);
                }
            }
            lblPortrait.setIcon(new ImageIcon(portrait));
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private void fillInfo() {	
        
        pnlInfo = new JPanel(new GridBagLayout());
        pnlInfo.setBorder(BorderFactory.createTitledBorder(person.getFullTitle()));
        pnlInfo.setBackground(Color.WHITE);
        lblType = new JLabel();
        lblStatus1 = new JLabel();
        lblStatus2 = new JLabel();
        lblOrigin1 = new JLabel();
        lblOrigin2 = new JLabel();
        lblCall1 = new JLabel();
        lblCall2 = new JLabel();
        lblAge1 = new JLabel();
        lblAge2 = new JLabel();
        lblGender1 = new JLabel();
        lblGender2 = new JLabel();
        lblRecruited1 = new JLabel();
        lblRecruited2 = new JLabel();
        lblTimeServed1 = new JLabel();
        lblTimeServed2 = new JLabel();

        GridBagConstraints gridBagConstraints;
        
        int firsty=0;
        
        lblType.setName("lblType"); // NOI18N
        lblType.setText(String.format(resourceMap.getString("format.italic"), person.getRoleDesc())); //$NON-NLS-1$
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = firsty;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 0.0;
        gridBagConstraints.insets = new Insets(0, 0, 5, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInfo.add(lblType, gridBagConstraints);
        firsty++;

        lblStatus1.setName("lblStatus1"); // NOI18N
        lblStatus1.setText(resourceMap.getString("lblStatus1.text")); //$NON-NLS-1$
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = firsty;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInfo.add(lblStatus1, gridBagConstraints);

        lblStatus2.setName("lblStatus2"); // NOI18N
        lblStatus2.setText(person.getStatusName() + person.pregnancyStatus());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = firsty;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInfo.add(lblStatus2, gridBagConstraints);              
        firsty++;
        
        if (campaign.getCampaignOptions().showOriginFaction()) {
            lblOrigin1.setName("lblOrigin1"); // NOI18N
            lblOrigin1.setText(resourceMap.getString("lblOrigin1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblOrigin1, gridBagConstraints);

            lblOrigin2.setName("lblOrigin2"); // NOI18N
            lblOrigin2.setText(person.getOriginFaction().getFullName(person.getCampaign().getGameYear()));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblOrigin2, gridBagConstraints);
            firsty++;
        }
        
        if(!person.getCallsign().equals("-") && person.getCallsign().length() > 0) { //$NON-NLS-1$
            lblCall1.setName("lblCall1"); // NOI18N
            lblCall1.setText(resourceMap.getString("lblCall1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblCall1, gridBagConstraints);

            lblCall2.setName("lblCall2"); // NOI18N
            lblCall2.setText(person.getCallsign());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblCall2, gridBagConstraints);
            firsty++;
        }

        //report either due date or age
        if (person.getDueDate() != null) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String DueDate = df.format(person.getDueDate().getTime());

            lblDuedate1.setName("lblDuedate1");
            lblDuedate1.setText(resourceMap.getString("lblDuedate1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblDuedate1, gridBagConstraints);

            lblDuedate2.setName("lblDuedate2"); // NOI18N
            lblDuedate2.setText(DueDate);
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblDuedate2, gridBagConstraints);
        } else {  
            lblAge1.setName("lblAge1"); // NOI18N
            lblAge1.setText(resourceMap.getString("lblAge1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblAge1, gridBagConstraints);

            lblAge2.setName("lblAge2"); // NOI18N
            lblAge2.setText(Integer.toString(person.getAge(campaign.getCalendar())));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.weightx = 0.5;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInfo.add(lblAge2, gridBagConstraints);
        }
        firsty++;

        lblGender1.setName("lblGender1"); // NOI18N
        lblGender1.setText(resourceMap.getString("lblGender1.text")); //$NON-NLS-1$
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = firsty;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInfo.add(lblGender1, gridBagConstraints);

        lblGender2.setName("lblGender2"); // NOI18N
        lblGender2.setText(person.getGenderName());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = firsty;
        gridBagConstraints.weightx = 0.5;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInfo.add(lblGender2, gridBagConstraints);

        firsty--;
        if (campaign.getCampaignOptions().getUseTimeInService()) {
            if ((null != person.getRecruitmentAsString()) && !person.isDependent() && !person.isPrisoner() && !person.isBondsman()) {
                lblRecruited1.setName("lblRecruited1"); // NOI18N
                lblRecruited1.setText(resourceMap.getString("lblRecruited1.text"));
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlInfo.add(lblRecruited1, gridBagConstraints);

                lblRecruited2.setName("lblRecruited2"); // NOI18N
                lblRecruited2.setText(person.getRecruitmentAsString());
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new Insets(0, 10, 0, 0);
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlInfo.add(lblRecruited2, gridBagConstraints);

                firsty++;
                lblTimeServed1.setName("lblTimeServed1"); // NOI18N
                lblTimeServed1.setText(resourceMap.getString("lblTimeServed1.text"));
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlInfo.add(lblTimeServed1, gridBagConstraints);

                lblTimeServed2.setName("lblTimeServed2"); // NOI18N
                lblTimeServed2.setText(Integer.toString(person.getTimeInService(campaign.getCalendar())) + " years");
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new Insets(0, 10, 0, 0);
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlInfo.add(lblTimeServed2, gridBagConstraints);
            }
        }
    }

    private void fillFamily() {
        pnlFamily = new JPanel(new GridBagLayout());
        pnlFamily.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("pnlFamily.title")));
        pnlFamily.setBackground(Color.WHITE);

        //family panel
        lblSpouse1 = new JLabel();
        lblSpouse2 = new JLabel();
        lblChildren1 = new JLabel();
        lblChildren2 = new JLabel();
        lblDuedate1 = new JLabel();
        lblDuedate2 = new JLabel();

        GridBagConstraints gridBagConstraints;
        
        int firsty=0;
        
        if (person.hasSpouse()) {
            lblSpouse1.setName("lblSpouse1"); // NOI18N //$NON-NLS-1$
            lblSpouse1.setText(resourceMap.getString("lblSpouse1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblSpouse1, gridBagConstraints);

            lblSpouse2.setName("lblSpouse2"); // NOI18N //$NON-NLS-1$
            lblSpouse2.setText(person.getSpouse().getFullName());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblSpouse2, gridBagConstraints);
            firsty++;
        }

        if (campaign.getCampaignOptions().useParentage() && person.hasChildren()) {
            lblChildren1.setName("lblChildren1"); // NOI18N //$NON-NLS-1$
            lblChildren1.setText(resourceMap.getString("lblChildren1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblChildren1, gridBagConstraints);

            lblChildren2.setName("lblChildren2"); // NOI18N //$NON-NLS-1$
            lblChildren2.setText(person.getChildList());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlFamily.add(lblChildren2, gridBagConstraints);
            firsty++;
        }
    }
    
    private void fillSkills() {

        //skill panel
        pnlSkills = new JPanel(new GridBagLayout());
        pnlSkills.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("pnlSkills.title")));
        pnlSkills.setBackground(Color.WHITE);

        //abilities and implants
        lblAbility1 = new JLabel();
        lblAbility2 = new JLabel();
        lblImplants1 = new JLabel();
        lblImplants2 = new JLabel();
        lblTough1 = new JLabel();
        lblTough2 = new JLabel();
        lblEdge1 = new JLabel();
        lblEdge2 = new JLabel();
        lblEdgeAvail1 = new JLabel();
        lblEdgeAvail2 = new JLabel();

        GridBagConstraints gridBagConstraints;

        JLabel lblName;
        JLabel lblValue;

        int firsty=0;
        int colBreak = Math.max((int) Math.ceil(person.getSkillNumber() / 2.0)+1, 3);
        int addition = 0;
        double weight = 0.5;

        int j = 0;
        for(int i = 0; i < SkillType.getSkillList().length; i++) {      	
            if(person.hasSkill(SkillType.getSkillList()[i])) {
                j++;
                if(j == colBreak) {
                    addition = 2;
                    firsty = 0;
                    weight = 1.0;
                }
                lblName = new JLabel(
                    String.format(resourceMap.getString("format.itemHeader"), SkillType.getSkillList()[i])); //$NON-NLS-1$
                lblValue = new JLabel(person.getSkill(SkillType.getSkillList()[i]).toString());
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 0+addition;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlSkills.add(lblName, gridBagConstraints);
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 1+addition;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.weightx = weight;
                gridBagConstraints.insets = new Insets(0, 10, 0, 0);
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlSkills.add(lblValue, gridBagConstraints);
                firsty++;
            }
        }

        //reset firsty
        firsty = colBreak;

        if(campaign.getCampaignOptions().useAbilities() && person.countOptions(PilotOptions.LVL3_ADVANTAGES) > 0) {
            lblAbility1.setName("lblAbility1"); // NOI18N //$NON-NLS-1$
            lblAbility1.setText(resourceMap.getString("lblAbility1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblAbility1, gridBagConstraints);

            lblAbility2.setName("lblAbility2"); // NOI18N //$NON-NLS-1$
            lblAbility2.setText(person.getAbilityList(PilotOptions.LVL3_ADVANTAGES));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblAbility2, gridBagConstraints);
            firsty++;
        }

        if(campaign.getCampaignOptions().useImplants() && person.countOptions(PilotOptions.MD_ADVANTAGES) > 0) {
            lblImplants1.setName("lblImplants1"); // NOI18N
            lblImplants1.setText(resourceMap.getString("lblImplants1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblImplants1, gridBagConstraints);

            lblImplants2.setName("lblImplants2"); // NOI18N
            lblImplants2.setText(person.getAbilityList(PilotOptions.MD_ADVANTAGES));
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblImplants2, gridBagConstraints);
            firsty++;
        }

        if(campaign.getCampaignOptions().useEdge() && person.getEdge()>0) {
            lblEdge1.setName("lblEdge1"); // NOI18N //$NON-NLS-1$
            lblEdge1.setText(resourceMap.getString("lblEdge1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblEdge1, gridBagConstraints);

            lblEdge2.setName("lblEdge2"); // NOI18N //$NON-NLS-1$
            lblEdge2.setText(Integer.toString(person.getEdge()));
            lblEdge2.setToolTipText(person.getEdgeTooltip());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 1;
            gridBagConstraints.weightx = 0.5;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblEdge2, gridBagConstraints);

            if (campaign.getCampaignOptions().useSupportEdge() && person.isSupport()) {
                //Add the Edge Available field for support personnel only
                lblEdgeAvail1.setName("lblEdgeAvail1"); // NOI18N //$NON-NLS-1$
                lblEdgeAvail1.setText(resourceMap.getString("lblEdgeAvail1.text")); //$NON-NLS-1$
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 2;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlSkills.add(lblEdgeAvail1, gridBagConstraints);

                lblEdgeAvail2.setName("lblEdgeAvail2"); // NOI18N //$NON-NLS-1$
                lblEdgeAvail2.setText(Integer.toString(person.getCurrentEdge()));
                gridBagConstraints = new GridBagConstraints();
                gridBagConstraints.gridx = 3;
                gridBagConstraints.gridy = firsty;
                gridBagConstraints.gridwidth = 1;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new Insets(0, 10, 0, 0);
                gridBagConstraints.fill = GridBagConstraints.NONE;
                gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
                pnlSkills.add(lblEdgeAvail2, gridBagConstraints);
            }
            firsty++;
        }
 
        if(campaign.getCampaignOptions().useToughness()) {
            lblTough1.setName("lblTough1"); // NOI18N
            lblTough1.setText(resourceMap.getString("lblTough1.text")); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblTough1, gridBagConstraints);

            lblTough2.setName("lblTough2"); // NOI18N //$NON-NLS-1$
            lblTough2.setText("+" + Integer.toString(person.getToughness())); //$NON-NLS-1$
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = firsty;
            gridBagConstraints.gridwidth = 3;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlSkills.add(lblTough2, gridBagConstraints);
            firsty++;
        }
        
    }
 
    private void fillLog() {
        ArrayList<LogEntry> logs = person.getPersonnelLog();
        pnlLog.setLayout(new GridBagLayout());
        PersonnelEventLogModel eventModel = new PersonnelEventLogModel();
        eventModel.setData(logs);
        JTable eventTable = new JTable(eventModel);
        eventTable.setRowSelectionAllowed(false);
        eventTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumn column = null;
        for(int i = 0; i < eventModel.getColumnCount(); ++ i) {
            column = eventTable.getColumnModel().getColumn(i);
            column.setCellRenderer(eventModel.getRenderer());
            column.setPreferredWidth(eventModel.getPreferredWidth(i));
            if(eventModel.hasConstantWidth(i)) {
                column.setMinWidth(eventModel.getPreferredWidth(i));
                column.setMaxWidth(eventModel.getPreferredWidth(i));
            }
        }
        eventTable.setIntercellSpacing(new Dimension(0, 0));
        eventTable.setShowGrid(false);
        eventTable.setTableHeader(null);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        pnlLog.add(eventTable, gridBagConstraints);
    }

    private void fillMissionLog() {
        List<LogEntry> missionLog = person.getMissionLog();
        pnlMissionsLog.setLayout(new GridBagLayout());

        JLabel lblMissions = new JLabel(String.format(resourceMap.getString("format.missions"), missionLog.size())); //$NON-NLS-1$
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlMissionsLog.add(lblMissions, gridBagConstraints);

        PersonnelEventLogModel eventModel = new PersonnelEventLogModel();
        eventModel.setData(missionLog);
        JTable missionsTable = new JTable(eventModel);
        missionsTable.setRowSelectionAllowed(false);
        missionsTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumn column;
        for(int i = 0; i < eventModel.getColumnCount(); ++ i) {
            column = missionsTable.getColumnModel().getColumn(i);
            column.setCellRenderer(eventModel.getRenderer());
            column.setPreferredWidth(eventModel.getPreferredWidth(i));
            if(eventModel.hasConstantWidth(i)) {
                column.setMinWidth(eventModel.getPreferredWidth(i));
                column.setMaxWidth(eventModel.getPreferredWidth(i));
            }
        }
        missionsTable.setIntercellSpacing(new Dimension(0, 0));
        missionsTable.setShowGrid(false);
        missionsTable.setTableHeader(null);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        pnlMissionsLog.add(missionsTable, gridBagConstraints);
    }

    private void fillInjuries() {

        pnlInjuries = new JPanel(new BorderLayout());
        pnlInjuries.setBorder(BorderFactory.createTitledBorder(resourceMap.getString("pnlInjuries.title"))); //$NON-NLS-1$
        pnlInjuries.setBackground(Color.WHITE);
        
        JButton medicalButton = new JButton(new ImageIcon("data/images/misc/medical.png")); //$NON-NLS-1$
        medicalButton.addActionListener(event -> {
            MedicalViewDialog medDialog = new MedicalViewDialog(SwingUtilities.getWindowAncestor(this), campaign, person, ip);
            medDialog.setGMMode(campaign.isGM());
            medDialog.setModalityType(ModalityType.APPLICATION_MODAL);
            medDialog.setVisible(true);
            removeAll();
            repaint();
            revalidate();
            initComponents();
            revalidate();
            MekHQ.triggerEvent(new PersonChangedEvent(person));
        });
        medicalButton.setMaximumSize(new Dimension(32, 32));
        medicalButton.setBackground(Color.WHITE);
        medicalButton.setMargin(new Insets(0, 0, 0, 0));
        medicalButton.setToolTipText(resourceMap.getString("btnMedical.tooltip")); //$NON-NLS-1$
        medicalButton.setAlignmentY(Component.TOP_ALIGNMENT);
        pnlInjuries.add(medicalButton, BorderLayout.LINE_START);
        
        JPanel pnlInjuryDetails = new JPanel(new GridBagLayout());
        pnlInjuryDetails.setBackground(Color.WHITE);
        pnlInjuryDetails.setAlignmentY(Component.TOP_ALIGNMENT);

        
        lblAdvancedMedical1 = new JLabel();
        lblAdvancedMedical2 = new JLabel();
        
        GridBagConstraints gridBagConstraints;
        
        lblAdvancedMedical1.setName("lblAdvancedMedical1"); // NOI18N
        lblAdvancedMedical1.setText(resourceMap.getString("lblAdvancedMedical1.text")); //$NON-NLS-1$
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInjuryDetails.add(lblAdvancedMedical1, gridBagConstraints);
        
        double vweight = 1.0;
        if(person.hasInjuries(false)) {
        	vweight = 0.0;
        }
        
        lblAdvancedMedical2.setName("lblAdvancedMedical2"); // NOI18N
        lblAdvancedMedical2.setText(person.getEffectString());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = vweight;
        gridBagConstraints.insets = new Insets(0, 10, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlInjuryDetails.add(lblAdvancedMedical2, gridBagConstraints);
        
        JLabel lblInjury;
        JLabel txtInjury;
        int row = 1;
        ArrayList<Injury> injuries = person.getInjuries();
        for(Injury injury : injuries) {
            lblInjury = new JLabel(injury.getFluff());
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = row;
            gridBagConstraints.weightx = 0.0;
            gridBagConstraints.insets = new Insets(0, 10, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.NONE;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInjuryDetails.add(lblInjury, gridBagConstraints);

            String text = (injury.isPermanent() && injury.getTime() < 1) ?
                resourceMap.getString("lblPermanentInjury.text") //$NON-NLS-1$
                : String.format(resourceMap.getString("format.injuryTime"), injury.getTime()); //$NON-NLS-1$
            txtInjury = new JLabel("<html>" + text + "</html>");
            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = row;
            gridBagConstraints.weightx = 1.0;
            if(row == (injuries.size() - 1)) {
                gridBagConstraints.weighty = 1.0;
            }
            gridBagConstraints.insets = new Insets(0, 20, 0, 0);
            gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            pnlInjuryDetails.add(txtInjury, gridBagConstraints);
            row++;
        }
        
        pnlInjuries.add(pnlInjuryDetails, BorderLayout.CENTER);
    }

    private void fillKillRecord() {
        List<Kill> kills = campaign.getKillsFor(person.getId());
        pnlKills.setLayout(new GridBagLayout());

        JLabel lblRecord = new JLabel(String.format(resourceMap.getString("format.kills"), kills.size())); //$NON-NLS-1$
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(0, 5, 0, 0);
        gridBagConstraints.fill = GridBagConstraints.NONE;
        gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
        pnlKills.add(lblRecord, gridBagConstraints);

        PersonnelKillLogModel killModel = new PersonnelKillLogModel();
        killModel.setData(kills);
        JTable killTable = new JTable(killModel);
        killTable.setRowSelectionAllowed(false);
        killTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        TableColumn column;
        for(int i = 0; i < killModel.getColumnCount(); ++ i) {
            column = killTable.getColumnModel().getColumn(i);
            column.setCellRenderer(killModel.getRenderer());
            column.setPreferredWidth(killModel.getPreferredWidth(i));
            if(killModel.hasConstantWidth(i)) {
                column.setMinWidth(killModel.getPreferredWidth(i));
                column.setMaxWidth(killModel.getPreferredWidth(i));
            }
        }
        killTable.setIntercellSpacing(new Dimension(0, 0));
        killTable.setShowGrid(false);
        killTable.setTableHeader(null);
        gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;

        pnlKills.add(killTable, gridBagConstraints);
    }
}
