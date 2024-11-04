import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Game extends JFrame {
    private GameLayer gamepanel,assetlayer;
    private Thread mainthread;
    private KeyPressed keydown = new KeyPressed();
    private ArrayList<Room> rooms = new ArrayList<>();
    private Camera camera = new Camera();
    private boolean transitionFlag = false;



    private Player player;

    private int currentRoom = 0;

    public Game() throws IOException {
        gamevalue();
        initialize();
        otherstuff();
    }

    public void initialize() {
        player.setInteraction(new Interaction() {

            @Override
            public void interact() {
                // TODO Auto-generated method stub

            }

            @Override
            public void moveroom(Exit exit) {
                transitionFlag = true;
                currentRoom = exit.getNextRoomNo();
                player.AdjustPosition(exit.getEnterPoint(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight(),camera.getXViewport(),camera.getYViewport());
                camera.AdjustPosition(player.getLocation(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight());
                transitionFlag = false;
            }
        });
        Container c = getContentPane();
        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_Z:
                        player.interact();
                        break;
                    case KeyEvent.VK_UP:

                        keydown.setUp(true);
                        break;
                    case KeyEvent.VK_DOWN:
                        keydown.setBottom(true);
                        break;
                    case KeyEvent.VK_RIGHT:
                        keydown.setRight(true);
                        break;
                    case KeyEvent.VK_LEFT:
                        keydown.setLeft(true);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        keydown.setUp(false);
                        break;
                    case KeyEvent.VK_DOWN:
                        keydown.setBottom(false);
                        break;
                    case KeyEvent.VK_RIGHT:
                        keydown.setRight(false);
                        break;
                    case KeyEvent.VK_LEFT:
                        keydown.setLeft(false);
                        break;
                }
            }
        });
        gamepanel = new GameLayer();
        gamepanel.setLocation(0,0);
        gamepanel.setRender(new IRender() {
            @Override
            public void render(Graphics2D graphics2D) {
                System.out.println(player.getPosition());
                System.out.println(getXViewPortStart() + " " + getYViewPortStart() + " " + getXViewPortEnd() + " " + getYViewPortEnd());
                graphics2D.drawImage(rooms.get(currentRoom).getImage(),getImageXStart(),getImageYStart(),getImageXEnd(),getImageYEnd(),getXViewPortStart(),getYViewPortStart(),getXViewPortEnd(),getYViewPortEnd(),null);
                graphics2D.drawImage(player.getImage(),player.getPosition().x-34,player.getPosition().y-39,68,78,null);
            }
        });
//        assetlayer = new GameLayer();
//        assetlayer.setSize(70,100);
//        assetlayer.setRender(new IRender() {
//            @Override
//            public void render(Graphics2D graphics2D) {
//                graphics2D.drawImage(player.getImage(),0,0,70,100,null);
//            }
//        });
        c.setLayout(new FlowLayout());
        c.add(gamepanel);
        //c.add(assetlayer);
        setVisible(true);
        setTitle(getshakespearepoem.getshakespearepoem());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000,740));
        pack();
    }

    public void gamevalue() throws IOException {
        player = new Player("(baka).png",68,78);
        rooms.add(new Room("maps/map.png",new Point(1500,350)));
        rooms.add(new Room("maps/bruh.png", new Point(709,1000)));
        rooms.add(new Room("maps/lol.png", new Point(500,350)));
        //rooms.get(0).getAssets().add(new Exit(70,300,1560,260) {{ setNextRoomNo(1); setEnterPoint(new Point(100,350)); }});
        loadRoomJson();
        rooms.get(0).loadCollision(rooms.get(0).getImage());
        rooms.get(1).loadCollision(ImageIO.read(new File("map_collision/bruh.png")));
        rooms.get(2).loadCollision(rooms.get(2).getImage());
        player.AdjustPosition(rooms.get(currentRoom).getSpawnPoint(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight(),camera.getXViewport(),camera.getYViewport());
        camera.AdjustPosition(player.getLocation(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight());
        //player.setPosition(rooms.get(currentRoom).getSpawnPoint());
        System.out.println(player.getLocation());
        //camera.setPosition(new Point(500,350));
    }

    public void loadRoomJson() {
        try {
            JSONObject json = new JSONObject(new String(Files.readAllBytes(Paths.get("rooms.json"))));
            JSONArray array = json.getJSONArray("rooms");
            for(int i = 0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                JSONArray exitsArray = obj.getJSONArray("exits");
                for (int j = 0; j < exitsArray.length(); j++) {
                    JSONObject exitObject = exitsArray.getJSONObject(j);
                    JSONObject enterPoint = exitObject.getJSONObject("enterPoint");
                    rooms.get(obj.getInt("roomNo")).getAssets().add(new Exit(exitObject.getInt("Width"), exitObject.getInt("Height"), exitObject.getInt("x"), exitObject.getInt("y")) {{ setNextRoomNo(exitObject.getInt("NextRoomNo")); setEnterPoint(new Point(enterPoint.getInt("x"),enterPoint.getInt("y"))); }});
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void otherstuff() {
        mainthread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    //player.move(keydown.isLeft(),keydown.isRight(),keydown.isUp(),keydown.isBottom(),getXViewPortStart(),getYViewPortStart(),getXViewPortEnd(),getYViewPortEnd(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight());
                    if(transitionFlag) continue;
                    if(player.move(keydown.isLeft(),keydown.isRight(),keydown.isUp(),keydown.isBottom(),getXViewPortStart(),getYViewPortStart(),getXViewPortEnd(),getYViewPortEnd(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight(),rooms.get(currentRoom))) {
                        camera.AdjustPosition(player.getLocation(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight());
                        //camera.move(keydown.isLeft(),keydown.isRight(),keydown.isUp(),keydown.isBottom(),player.getPosition(),rooms.get(currentRoom).getImage().getWidth(),rooms.get(currentRoom).getImage().getHeight());
                    }

                    //camera.setPosition(new Point(getXViewPortEnd()/2,getYViewPortEnd()/2));

                    //assetlayer.setLocation(player.getPosition());
                    gamepanel.repaint();
                    //assetlayer.repaint();
                    try {
                        Thread.sleep(32);
                    }catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        mainthread.start();
    }

    private int getXViewPortStart() {

        return Math.max(camera.getPosition().x - camera.getXViewport(), 0);
    }

    private int getXViewPortEnd() {
        if(rooms.get(currentRoom).getImage().getWidth() < 1000) return rooms.get(currentRoom).getImage().getWidth();
        if((Math.min(camera.getPosition().x + camera.getXViewport(), rooms.get(currentRoom).getImage().getWidth())) < 1000) return 1000;

        return Math.min(camera.getPosition().x + camera.getXViewport(), rooms.get(currentRoom).getImage().getWidth());
    }

    private int getYViewPortStart() {

        return Math.max(camera.getPosition().y - camera.getYViewport(), 0);
    }

    private int getYViewPortEnd() {
        if(rooms.get(currentRoom).getImage().getHeight() < 700) return rooms.get(currentRoom).getImage().getHeight();
        if((Math.min(camera.getPosition().y + camera.getYViewport(), rooms.get(currentRoom).getImage().getHeight())) < 700) return 700;

        return Math.min(camera.getPosition().y + camera.getYViewport(), rooms.get(currentRoom).getImage().getHeight());
    }

    private int getImageXStart() {
        if(rooms.get(currentRoom).getImage().getWidth() < 1000) return (1000-rooms.get(currentRoom).getImage().getWidth())/2;
        return 0;
    }

    private int getImageXEnd() {
        if(rooms.get(currentRoom).getImage().getWidth() < 1000) return (1000-rooms.get(currentRoom).getImage().getWidth())/2+rooms.get(currentRoom).getImage().getWidth();
        return 1000;
    }

    private int getImageYStart() {
        if(rooms.get(currentRoom).getImage().getHeight() < 700) return (700-rooms.get(currentRoom).getImage().getHeight())/2;
        return 0;
    }

    private int getImageYEnd() {
        if(rooms.get(currentRoom).getImage().getHeight() < 700) return (700-rooms.get(currentRoom).getImage().getHeight())/2+rooms.get(currentRoom).getImage().getHeight();
        return 700;
    }
}

class GameLayer extends JPanel{
    public GameLayer() {
        setPreferredSize(new Dimension(1000,700));
        setBackground(Color.BLACK);
    }
    private IRender render;


    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D)graphics;

        render.render(g2d);
    }

    public void setRender(IRender r) {
        render = r;
    }
}

interface IRender {
    public void render(Graphics2D graphics2D);
}

class KeyPressed {
    private boolean left,right,up,bottom;

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isBottom() {
        return bottom;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }
}

class getshakespearepoem {
    public static String getshakespearepoem() {
        return "FROM off a hill whose concave womb reworded\n" +
                "A plaintful story from a sistering vale,\n" +
                "My spirits to attend this double voice accorded,\n" +
                "And down I laid to list the sad-tuned tale;\n" +
                "Ere long espied a fickle maid full pale,\n" +
                "Tearing of papers, breaking rings a-twain,\n" +
                "Storming her world with sorrow's wind and rain.\n" +
                "\n" +
                "Upon her head a platted hive of straw,\n" +
                "Which fortified her visage from the sun,\n" +
                "Whereon the thought might think sometime it saw\n" +
                "The carcass of beauty spent and done:\n" +
                "Time had not scythed all that youth begun,\n" +
                "Nor youth all quit; but, spite of heaven's fell rage,\n" +
                "Some beauty peep'd through lattice of sear'd age.\n" +
                "\n" +
                "Oft did she heave her napkin to her eyne,\n" +
                "Which on it had conceited characters,\n" +
                "Laundering the silken figures in the brine\n" +
                "That season'd woe had pelleted in tears,\n" +
                "And often reading what contents it bears;\n" +
                "As often shrieking undistinguish'd woe,\n" +
                "In clamours of all size, both high and low.\n" +
                "\n" +
                "Sometimes her levell'd eyes their carriage ride,\n" +
                "As they did battery to the spheres intend;\n" +
                "Sometime diverted their poor balls are tied\n" +
                "To the orbed earth; sometimes they do extend\n" +
                "Their view right on; anon their gazes lend\n" +
                "To every place at once, and, nowhere fix'd,\n" +
                "The mind and sight distractedly commix'd.\n" +
                "\n" +
                "Her hair, nor loose nor tied in formal plat,\n" +
                "Proclaim'd in her a careless hand of pride\n" +
                "For some, untuck'd, descended her sheaved hat,\n" +
                "Hanging her pale and pined cheek beside;\n" +
                "Some in her threaden fillet still did bide,\n" +
                "And true to bondage would not break from thence,\n" +
                "Though slackly braided in loose negligence.\n" +
                "\n" +
                "A thousand favours from a maund she drew\n" +
                "Of amber, crystal, and of beaded jet,\n" +
                "Which one by one she in a river threw,\n" +
                "Upon whose weeping margent she was set;\n" +
                "Like usury, applying wet to wet,\n" +
                "Or monarch's hands that let not bounty fall\n" +
                "Where want cries some, but where excess begs all.\n" +
                "\n" +
                "Of folded schedules had she many a one,\n" +
                "Which she perused, sigh'd, tore, and gave the flood;\n" +
                "Crack'd many a ring of posied gold and bone\n" +
                "Bidding them find their sepulchres in mud;\n" +
                "Found yet moe letters sadly penn'd in blood,\n" +
                "With sleided silk feat and affectedly\n" +
                "Enswathed, and seal'd to curious secrecy.\n" +
                "\n" +
                "These often bathed she in her fluxive eyes,\n" +
                "And often kiss'd, and often 'gan to tear:\n" +
                "Cried 'O false blood, thou register of lies,\n" +
                "What unapproved witness dost thou bear!\n" +
                "Ink would have seem'd more black and damned here!'\n" +
                "This said, in top of rage the lines she rents,\n" +
                "Big discontent so breaking their contents.\n" +
                "\n" +
                "A reverend man that grazed his cattle nigh--\n" +
                "Sometime a blusterer, that the ruffle knew\n" +
                "Of court, of city, and had let go by\n" +
                "The swiftest hours, observed as they flew--\n" +
                "Towards this afflicted fancy fastly drew,\n" +
                "And, privileged by age, desires to know\n" +
                "In brief the grounds and motives of her woe.\n" +
                "\n" +
                "So slides he down upon his grained bat,\n" +
                "And comely-distant sits he by her side;\n" +
                "When he again desires her, being sat,\n" +
                "Her grievance with his hearing to divide:\n" +
                "If that from him there may be aught applied\n" +
                "Which may her suffering ecstasy assuage,\n" +
                "'Tis promised in the charity of age.\n" +
                "\n" +
                "'Father,' she says, 'though in me you behold\n" +
                "The injury of many a blasting hour,\n" +
                "Let it not tell your judgment I am old;\n" +
                "Not age, but sorrow, over me hath power:\n" +
                "I might as yet have been a spreading flower,\n" +
                "Fresh to myself, If I had self-applied\n" +
                "Love to myself and to no love beside.\n" +
                "\n" +
                "'But, woe is me! too early I attended\n" +
                "A youthful suit--it was to gain my grace--\n" +
                "Of one by nature's outwards so commended,\n" +
                "That maidens' eyes stuck over all his face:\n" +
                "Love lack'd a dwelling, and made him her place;\n" +
                "And when in his fair parts she did abide,\n" +
                "She was new lodged and newly deified.\n" +
                "\n" +
                "'His browny locks did hang in crooked curls;\n" +
                "And every light occasion of the wind\n" +
                "Upon his lips their silken parcels hurls.\n" +
                "What's sweet to do, to do will aptly find:\n" +
                "Each eye that saw him did enchant the mind,\n" +
                "For on his visage was in little drawn\n" +
                "What largeness thinks in Paradise was sawn.\n" +
                "\n" +
                "'Small show of man was yet upon his chin;\n" +
                "His phoenix down began but to appear\n" +
                "Like unshorn velvet on that termless skin\n" +
                "Whose bare out-bragg'd the web it seem'd to wear:\n" +
                "Yet show'd his visage by that cost more dear;\n" +
                "And nice affections wavering stood in doubt\n" +
                "If best were as it was, or best without.\n" +
                "\n" +
                "'His qualities were beauteous as his form,\n" +
                "For maiden-tongued he was, and thereof free;\n" +
                "Yet, if men moved him, was he such a storm\n" +
                "As oft 'twixt May and April is to see,\n" +
                "When winds breathe sweet, untidy though they be.\n" +
                "His rudeness so with his authorized youth\n" +
                "Did livery falseness in a pride of truth.\n" +
                "\n" +
                "'Well could he ride, and often men would say\n" +
                "'That horse his mettle from his rider takes:\n" +
                "Proud of subjection, noble by the sway,\n" +
                "What rounds, what bounds, what course, what stop\n" +
                "he makes!'\n" +
                "And controversy hence a question takes,\n" +
                "Whether the horse by him became his deed,\n" +
                "Or he his manage by the well-doing steed.\n" +
                "\n" +
                "'But quickly on this side the verdict went:\n" +
                "His real habitude gave life and grace\n" +
                "To appertainings and to ornament,\n" +
                "Accomplish'd in himself, not in his case:\n" +
                "All aids, themselves made fairer by their place,\n" +
                "Came for additions; yet their purposed trim\n" +
                "Pieced not his grace, but were all graced by him.\n" +
                "\n" +
                "'So on the tip of his subduing tongue\n" +
                "All kinds of arguments and question deep,\n" +
                "All replication prompt, and reason strong,\n" +
                "For his advantage still did wake and sleep:\n" +
                "To make the weeper laugh, the laugher weep,\n" +
                "He had the dialect and different skill,\n" +
                "Catching all passions in his craft of will:\n" +
                "\n" +
                "'That he did in the general bosom reign\n" +
                "Of young, of old; and sexes both enchanted,\n" +
                "To dwell with him in thoughts, or to remain\n" +
                "In personal duty, following where he haunted:\n" +
                "Consents bewitch'd, ere he desire, have granted;\n" +
                "And dialogued for him what he would say,\n" +
                "Ask'd their own wills, and made their wills obey.\n" +
                "\n" +
                "'Many there were that did his picture get,\n" +
                "To serve their eyes, and in it put their mind;\n" +
                "Like fools that in th' imagination set\n" +
                "The goodly objects which abroad they find\n" +
                "Of lands and mansions, theirs in thought assign'd;\n" +
                "And labouring in moe pleasures to bestow them\n" +
                "Than the true gouty landlord which doth owe them:\n" +
                "\n" +
                "'So many have, that never touch'd his hand,\n" +
                "Sweetly supposed them mistress of his heart.\n" +
                "My woeful self, that did in freedom stand,\n" +
                "And was my own fee-simple, not in part,\n" +
                "What with his art in youth, and youth in art,\n" +
                "Threw my affections in his charmed power,\n" +
                "Reserved the stalk and gave him all my flower.\n" +
                "\n" +
                "'Yet did I not, as some my equals did,\n" +
                "Demand of him, nor being desired yielded;\n" +
                "Finding myself in honour so forbid,\n" +
                "With safest distance I mine honour shielded:\n" +
                "Experience for me many bulwarks builded\n" +
                "Of proofs new-bleeding, which remain'd the foil\n" +
                "Of this false jewel, and his amorous spoil.\n" +
                "\n" +
                "'But, ah, who ever shunn'd by precedent\n" +
                "The destined ill she must herself assay?\n" +
                "Or forced examples, 'gainst her own content,\n" +
                "To put the by-past perils in her way?\n" +
                "Counsel may stop awhile what will not stay;\n" +
                "For when we rage, advice is often seen\n" +
                "By blunting us to make our wits more keen.\n" +
                "\n" +
                "'Nor gives it satisfaction to our blood,\n" +
                "That we must curb it upon others' proof;\n" +
                "To be forbod the sweets that seem so good,\n" +
                "For fear of harms that preach in our behoof.\n" +
                "O appetite, from judgment stand aloof!\n" +
                "The one a palate hath that needs will taste,\n" +
                "Though Reason weep, and cry, 'It is thy last.'\n" +
                "\n" +
                "'For further I could say 'This man's untrue,'\n" +
                "And knew the patterns of his foul beguiling;\n" +
                "Heard where his plants in others' orchards grew,\n" +
                "Saw how deceits were gilded in his smiling;\n" +
                "Knew vows were ever brokers to defiling;\n" +
                "Thought characters and words merely but art,\n" +
                "And bastards of his foul adulterate heart.\n" +
                "\n" +
                "'And long upon these terms I held my city,\n" +
                "Till thus he gan besiege me: 'Gentle maid,\n" +
                "Have of my suffering youth some feeling pity,\n" +
                "And be not of my holy vows afraid:\n" +
                "That's to ye sworn to none was ever said;\n" +
                "For feasts of love I have been call'd unto,\n" +
                "Till now did ne'er invite, nor never woo.\n" +
                "\n" +
                "''All my offences that abroad you see\n" +
                "Are errors of the blood, none of the mind;\n" +
                "Love made them not: with acture they may be,\n" +
                "Where neither party is nor true nor kind:\n" +
                "They sought their shame that so their shame did find;\n" +
                "And so much less of shame in me remains,\n" +
                "By how much of me their reproach contains.\n" +
                "\n" +
                "''Among the many that mine eyes have seen,\n" +
                "Not one whose flame my heart so much as warm'd,\n" +
                "Or my affection put to the smallest teen,\n" +
                "Or any of my leisures ever charm'd:\n" +
                "Harm have I done to them, but ne'er was harm'd;\n" +
                "Kept hearts in liveries, but mine own was free,\n" +
                "And reign'd, commanding in his monarchy.\n" +
                "\n" +
                "''Look here, what tributes wounded fancies sent me,\n" +
                "Of paled pearls and rubies red as blood;\n" +
                "Figuring that they their passions likewise lent me\n" +
                "Of grief and blushes, aptly understood\n" +
                "In bloodless white and the encrimson'd mood;\n" +
                "Effects of terror and dear modesty,\n" +
                "Encamp'd in hearts, but fighting outwardly.\n" +
                "\n" +
                "''And, lo, behold these talents of their hair,\n" +
                "With twisted metal amorously impleach'd,\n" +
                "I have received from many a several fair,\n" +
                "Their kind acceptance weepingly beseech'd,\n" +
                "With the annexions of fair gems enrich'd,\n" +
                "And deep-brain'd sonnets that did amplify\n" +
                "Each stone's dear nature, worth, and quality.\n" +
                "\n" +
                "''The diamond,--why, 'twas beautiful and hard,\n" +
                "Whereto his invised properties did tend;\n" +
                "The deep-green emerald, in whose fresh regard\n" +
                "Weak sights their sickly radiance do amend;\n" +
                "The heaven-hued sapphire and the opal blend\n" +
                "With objects manifold: each several stone,\n" +
                "With wit well blazon'd, smiled or made some moan.\n" +
                "\n" +
                "''Lo, all these trophies of affections hot,\n" +
                "Of pensived and subdued desires the tender,\n" +
                "Nature hath charged me that I hoard them not,\n" +
                "But yield them up where I myself must render,\n" +
                "That is, to you, my origin and ender;\n" +
                "For these, of force, must your oblations be,\n" +
                "Since I their altar, you enpatron me.\n" +
                "\n" +
                "''O, then, advance of yours that phraseless hand,\n" +
                "Whose white weighs down the airy scale of praise;\n" +
                "Take all these similes to your own command,\n" +
                "Hallow'd with sighs that burning lungs did raise;\n" +
                "What me your minister, for you obeys,\n" +
                "Works under you; and to your audit comes\n" +
                "Their distract parcels in combined sums.\n" +
                "\n" +
                "''Lo, this device was sent me from a nun,\n" +
                "Or sister sanctified, of holiest note;\n" +
                "Which late her noble suit in court did shun,\n" +
                "Whose rarest havings made the blossoms dote;\n" +
                "For she was sought by spirits of richest coat,\n" +
                "But kept cold distance, and did thence remove,\n" +
                "To spend her living in eternal love.\n" +
                "\n" +
                "''But, O my sweet, what labour is't to leave\n" +
                "The thing we have not, mastering what not strives,\n" +
                "Playing the place which did no form receive,\n" +
                "Playing patient sports in unconstrained gyves?\n" +
                "She that her fame so to herself contrives,\n" +
                "The scars of battle 'scapeth by the flight,\n" +
                "And makes her absence valiant, not her might.\n" +
                "\n" +
                "''O, pardon me, in that my boast is true:\n" +
                "The accident which brought me to her eye\n" +
                "Upon the moment did her force subdue,\n" +
                "And now she would the caged cloister fly:\n" +
                "Religious love put out Religion's eye:\n" +
                "Not to be tempted, would she be immured,\n" +
                "And now, to tempt, all liberty procured.\n" +
                "\n" +
                "''How mighty then you are, O, hear me tell!\n" +
                "The broken bosoms that to me belong\n" +
                "Have emptied all their fountains in my well,\n" +
                "And mine I pour your ocean all among:\n" +
                "I strong o'er them, and you o'er me being strong,\n" +
                "Must for your victory us all congest,\n" +
                "As compound love to physic your cold breast.\n" +
                "\n" +
                "''My parts had power to charm a sacred nun,\n" +
                "Who, disciplined, ay, dieted in grace,\n" +
                "Believed her eyes when they to assail begun,\n" +
                "All vows and consecrations giving place:\n" +
                "O most potential love! vow, bond, nor space,\n" +
                "In thee hath neither sting, knot, nor confine,\n" +
                "For thou art all, and all things else are thine.\n" +
                "\n" +
                "''When thou impressest, what are precepts worth\n" +
                "Of stale example? When thou wilt inflame,\n" +
                "How coldly those impediments stand forth\n" +
                "Of wealth, of filial fear, law, kindred, fame!\n" +
                "Love's arms are peace, 'gainst rule, 'gainst sense,\n" +
                "'gainst shame,\n" +
                "And sweetens, in the suffering pangs it bears,\n" +
                "The aloes of all forces, shocks, and fears.\n" +
                "\n" +
                "''Now all these hearts that do on mine depend,\n" +
                "Feeling it break, with bleeding groans they pine;\n" +
                "And supplicant their sighs to you extend,\n" +
                "To leave the battery that you make 'gainst mine,\n" +
                "Lending soft audience to my sweet design,\n" +
                "And credent soul to that strong-bonded oath\n" +
                "That shall prefer and undertake my troth.'\n" +
                "\n" +
                "'This said, his watery eyes he did dismount,\n" +
                "Whose sights till then were levell'd on my face;\n" +
                "Each cheek a river running from a fount\n" +
                "With brinish current downward flow'd apace:\n" +
                "O, how the channel to the stream gave grace!\n" +
                "Who glazed with crystal gate the glowing roses\n" +
                "That flame through water which their hue encloses.\n" +
                "\n" +
                "'O father, what a hell of witchcraft lies\n" +
                "In the small orb of one particular tear!\n" +
                "But with the inundation of the eyes\n" +
                "What rocky heart to water will not wear?\n" +
                "What breast so cold that is not warmed here?\n" +
                "O cleft effect! cold modesty, hot wrath,\n" +
                "Both fire from hence and chill extincture hath.\n" +
                "\n" +
                "'For, lo, his passion, but an art of craft,\n" +
                "Even there resolved my reason into tears;\n" +
                "There my white stole of chastity I daff'd,\n" +
                "Shook off my sober guards and civil fears;\n" +
                "Appear to him, as he to me appears,\n" +
                "All melting; though our drops this difference bore,\n" +
                "His poison'd me, and mine did him restore.\n" +
                "\n" +
                "'In him a plenitude of subtle matter,\n" +
                "Applied to cautels, all strange forms receives,\n" +
                "Of burning blushes, or of weeping water,\n" +
                "Or swooning paleness; and he takes and leaves,\n" +
                "In either's aptness, as it best deceives,\n" +
                "To blush at speeches rank to weep at woes,\n" +
                "Or to turn white and swoon at tragic shows.\n" +
                "\n" +
                "'That not a heart which in his level came\n" +
                "Could 'scape the hail of his all-hurting aim,\n" +
                "Showing fair nature is both kind and tame;\n" +
                "And, veil'd in them, did win whom he would maim:\n" +
                "Against the thing he sought he would exclaim;\n" +
                "When he most burn'd in heart-wish'd luxury,\n" +
                "He preach'd pure maid, and praised cold chastity.\n" +
                "\n" +
                "'Thus merely with the garment of a Grace\n" +
                "The naked and concealed fiend he cover'd;\n" +
                "That th' unexperient gave the tempter place,\n" +
                "Which like a cherubin above them hover'd.\n" +
                "Who, young and simple, would not be so lover'd?\n" +
                "Ay me! I fell; and yet do question make\n" +
                "What I should do again for such a sake.\n" +
                "\n" +
                "'O, that infected moisture of his eye,\n" +
                "O, that false fire which in his cheek so glow'd,\n" +
                "O, that forced thunder from his heart did fly,\n" +
                "O, that sad breath his spongy lungs bestow'd,\n" +
                "O, all that borrow'd motion seeming owed,\n" +
                "Would yet again betray the fore-betray'd,\n" +
                "And new pervert a reconciled maid!'";
    }
}