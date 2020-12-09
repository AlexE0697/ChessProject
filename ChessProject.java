import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

/*
	This class can be used as a starting point for creating your Chess game project. The only piece that 
	has been coded is a white pawn...a lot done, more to do!
*/

public class ChessProject extends JFrame implements MouseListener, MouseMotionListener {
    static String AIOption;
    Boolean white2Move = true;
    AIAgent agent = new AIAgent();
    JLayeredPane layeredPane;
    JPanel chessBoard;
    JLabel chessPiece;
    int xAdjustment;
    int yAdjustment;
    int startX;
    int startY;
    int initialX;
    int initialY;
    JPanel panels;
    JLabel pieces;


    public ChessProject() {
        Dimension boardSize = new Dimension(600, 600);

        //  Use a Layered Pane for this application
        layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane);
        layeredPane.setPreferredSize(boardSize);
        layeredPane.addMouseListener(this);
        layeredPane.addMouseMotionListener(this);

        //Add a chess board to the Layered Pane
        chessBoard = new JPanel();
        layeredPane.add(chessBoard, JLayeredPane.DEFAULT_LAYER);
        chessBoard.setLayout(new GridLayout(8, 8));
        chessBoard.setPreferredSize(boardSize);
        chessBoard.setBounds(0, 0, boardSize.width, boardSize.height);

        for (int i = 0; i < 64; i++) {
            JPanel square = new JPanel(new BorderLayout());
            chessBoard.add(square);

            int row = (i / 8) % 2;
            if (row == 0)
                square.setBackground(i % 2 == 0 ? Color.white : Color.gray);
            else
                square.setBackground(i % 2 == 0 ? Color.gray : Color.white);
        }

        // Setting up the Initial Chess board.
        for (int i = 8; i < 16; i++) {
            pieces = new JLabel(new ImageIcon("WhitePawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(0);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(1);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKnight.png"));
        panels = (JPanel) chessBoard.getComponent(6);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
        panels = (JPanel) chessBoard.getComponent(2);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteBishup.png"));
        panels = (JPanel) chessBoard.getComponent(5);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteKing.png"));
        panels = (JPanel) chessBoard.getComponent(3);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
        panels = (JPanel) chessBoard.getComponent(4);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("WhiteRook.png"));
        panels = (JPanel) chessBoard.getComponent(7);
        panels.add(pieces);
        for (int i = 48; i < 56; i++) {
            pieces = new JLabel(new ImageIcon("BlackPawn.png"));
            panels = (JPanel) chessBoard.getComponent(i);
            panels.add(pieces);
        }
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(56);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(57);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKnight.png"));
        panels = (JPanel) chessBoard.getComponent(62);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishup.png"));
        panels = (JPanel) chessBoard.getComponent(58);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackBishup.png"));
        panels = (JPanel) chessBoard.getComponent(61);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackKing.png"));
        panels = (JPanel) chessBoard.getComponent(59);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackQueen.png"));
        panels = (JPanel) chessBoard.getComponent(60);
        panels.add(pieces);
        pieces = new JLabel(new ImageIcon("BlackRook.png"));
        panels = (JPanel) chessBoard.getComponent(63);
        panels.add(pieces);
    }


    /*
        This method checks if there is a piece present on a particular square.
    */
    private Boolean piecePresent(int x, int y) {
        Component c = chessBoard.findComponentAt(x, y);
        if (c instanceof JPanel) {
            return false;
        } else {
            return true;
        }
    }

    private String newPiece(int x, int y) {
        x = x * 75;
        y = y * 75;
        if (chessBoard.findComponentAt(x, y) instanceof JLabel) {
            Component newC = chessBoard.findComponentAt(x, y);
            JLabel awaitingPiece = (JLabel) newC;
            return awaitingPiece.getIcon().toString();
        } else {
            return "";
        }
    }

    /*
        This is a method to check if a piece is a Black piece.
    */
    private Boolean checkWhiteOponent(int newX, int newY) {
        Boolean oponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("Black")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }

    /*
       This is a method to check if a piece is a White piece.
   */
    private Boolean checkBlackOponent(int newX, int newY) {
        Boolean oponent;
        Component c1 = chessBoard.findComponentAt(newX, newY);
        JLabel awaitingPiece = (JLabel) c1;
        String tmp1 = awaitingPiece.getIcon().toString();
        if (((tmp1.contains("White")))) {
            oponent = true;
        } else {
            oponent = false;
        }
        return oponent;
    }

    /*
        This method is called when we press the Mouse. So we need to find out what piece we have
        selected. We may also not have selected a piece!
    */
    public void mousePressed(MouseEvent e) {
        chessPiece = null;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        if (c instanceof JPanel)
            return;

        Point parentLocation = c.getParent().getLocation();
        xAdjustment = parentLocation.x - e.getX();
        yAdjustment = parentLocation.y - e.getY();
        chessPiece = (JLabel) c;
        initialX = e.getX();
        initialY = e.getY();
        startX = (e.getX() / 75);
        startY = (e.getY() / 75);
        chessPiece.setLocation(e.getX() + xAdjustment, e.getY() + yAdjustment);
        chessPiece.setSize(chessPiece.getWidth(), chessPiece.getHeight());
        layeredPane.add(chessPiece, JLayeredPane.DRAG_LAYER);
    }

    public void mouseDragged(MouseEvent me) {
        if (chessPiece == null) return;
        chessPiece.setLocation(me.getX() + xAdjustment, me.getY() + yAdjustment);
    }

    /*
       This method is used when the Mouse is released...we need to make sure the move was valid before
       putting the piece back on the board.
   */

    //global variables
    Boolean validMove = false;
    Boolean success;
    String pieceName;

    public void mouseReleased(MouseEvent e) {
        if (chessPiece == null) return;
        chessPiece.setVisible(false);
        success = false;
        Component c = chessBoard.findComponentAt(e.getX(), e.getY());
        String tmp = chessPiece.getIcon().toString();
        pieceName = tmp.substring(0, (tmp.length() - 4));
        validMove = false;


        if (AIOption.contains("Random") || (AIOption.contains("NextBest")) || (AIOption.contains("TwoDeep"))) {
            switch (pieceName) {
                case ("WhitePawn"):
                    whitePawnMethod(e);
                    break;
                case ("WhiteRook"):
                    RookMethod(e);
                    break;
                case ("WhiteKnight"):
                    KnightMethod(e);
                    break;
                case ("WhiteBishup"):
                    BishopMethod(e);
                    break;
                case ("WhiteQueen"):
                    QueenMethod(e);
                    break;
                case ("WhiteKing"):
                    KingMethod(e);
                    break;
            }//end switch for white piece

        } else {
            switch (pieceName) {
                case ("BlackPawn"):
                    blackPawnMethod(e);
                    break;
                case ("BlackRook"):
                    RookMethod(e);
                    break;
                case ("BlackKnight"):
                    KnightMethod(e);
                    break;
                case ("BlackBishup"):
                    BishopMethod(e);
                    break;
                case ("BlackQueen"):
                    QueenMethod(e);
                    break;
                case ("BlackKing"):
                    KingMethod(e);
                    break;
            }//end switch for black pieces
        }//end else  for turn statement

        if (!validMove) {
            int location = 0;
            if (startY == 0) {
                location = startX;
            } else {
                location = (startY * 8) + startX;
            }
            String pieceLocation = pieceName + ".png";
            pieces = new JLabel(new ImageIcon(pieceLocation));
            panels = (JPanel) chessBoard.getComponent(location);
            panels.add(pieces);
        } //end if not a valid move
        else {
            if ((success) && (pieceName.contains("White"))) {
                int location = 56 + (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("WhiteQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }
            } else if ((success) && (pieceName.contains("Black"))) {
                int location = (e.getX() / 75);
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                } else {
                    Container parent = (Container) c;
                    pieces = new JLabel(new ImageIcon("BlackQueen.png"));
                    parent = (JPanel) chessBoard.getComponent(location);
                    parent.add(pieces);
                }

            } else {
                if (c instanceof JLabel) {
                    Container parent = c.getParent();
                    parent.remove(0);
                    parent.add(chessPiece);
                } else {
                    Container parent = (Container) c;
                    parent.add(chessPiece);
                }
                chessPiece.setVisible(true);
            }

            makeAIMove();
        }//end else for if a valid move

    }//end mouseReleased method

    private void whitePawnMethod(MouseEvent e) { //whitePawn method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);
        int xMovement = Math.abs((e.getX() / 75) - startX);
        int yMovement = Math.abs((e.getY() / 75) - startY);

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if (startY == 1) { //if white pawn is in its first position
            if ((startX == newX) && (((newY - startY) == 1) || (newY - startY) == 2)) { //if the white pawn moves one OR two squares on the y axis
                if (newY - startY == 2) { // if the white piece moves two squares on the y axis
                    if (!piecePresent(e.getX(), e.getY()) && (!piecePresent(e.getX(), e.getY() - 75))) { // check if no pieces are present in the two squares the white pawn jumps
                        validMove = true; //allow the move
                    }//end if
                    else { //else dont allow the move
                        validMove = false;
                    }
                }//end if the pawn moves two squares
                else { //if the white pawn has not moved 2 squares
                    if (!piecePresent(e.getX(), e.getY())) {//if no piece is present allow the pawn to move one square
                        validMove = true; //alow the move
                    }//end if
                    else { //else dont allow the move
                        validMove = false;
                    }//end else
                }//end else for the pawn to move one square
            }//end if the pawn has moved one OR two squares forward
            //if there is a piece present down to the right or down to the left and withing the boundaries of the board
            else if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7))) && (newY - startY == 1) || ((newX == (startX - 1)) && (startX - 1 >= 0)))) && (newY - startY == 1)) {
                if (checkWhiteOponent(e.getX(), e.getY())) { //check if this is a black piece
                    validMove = true; //allow the white pawn to take the black pawn
                } else {
                    validMove = false; //don't allow the white pawn to take the black pawn
                }
            } else { //else if there are no pieces diagonal to the white pawn from the starting position
                validMove = false; // don't allow a move
            }
        }//end if the white pawn is in the first position
        else {//if the white pawn is not in the first position
            if ((startX - 1 >= 0) || (startX + 1 <= 7)) { //ensure the pawn stays on the board over the x axis
                // if there is a piece present down and to the right or down and to the left and within the boundaries of the board
                if ((piecePresent(e.getX(), (e.getY()))) && ((((newX == (startX + 1) && (startX + 1 <= 7) && (newY == startY + 1))) || ((newX == (startX - 1)) && (startX - 1 >= 0) && (newY == startY + 1))))) {
                    if (checkWhiteOponent(e.getX(), e.getY())) { //if its a black piece
                        validMove = true;//allow it to be taken
                        if (startY == 6) { //if the pawn was on the the position of 6 while taking a piece
                            success = true; // this allows the pawn to transform
                        }
                    } else {
                        validMove = false;
                    }
                } //end if for taking a piece when on the starting y position of 6
                else { //otherwise
                    if (!piecePresent(e.getX(), (e.getY()))) { //if there is no piece present
                        if ((startX == (e.getX() / 75)) && ((e.getY() / 75) - startY) == 1) { //if the pawn moves forward one square
                            if (startY == 6) { //and its one the starting y position of 6
                                success = true;// this allows the pawn to transform
                            }
                            validMove = true; //allow the move
                        } else {
                            validMove = false; //dont allow the move if the pawn does not move one position
                        }
                    } else {
                        validMove = false; //dont allow the pawn to move if there is a piece present in front of it
                    }
                }// end else for taking a piece and transforming into a queen
            } else {
                validMove = false; //dont allow the pawn to move if it does not take a piece
            }
        }//end else if the pawn is not in the first position
    }//end white pawn method

    private void blackPawnMethod(MouseEvent e) {//Blackpawn method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if (startY == 6) { // if the black pawn is in its first position
            if ((startX == newX) && (((startY - newY) == 1) || (startY - newY) == 2)) { //if the black pawn can move one or two squares from the starting position
                //If there is a piece in the way
                if ((!piecePresent(e.getX(), e.getY())) && (!piecePresent(e.getX(), e.getY() + 75))) { // if there is no piece present on an X coordinate or Y coordinate and there is no piece present on an X coordinate or Y coordinate + 1 tile
                    validMove = true; //the move can be made
                }//end if
            } //end
            else {
                validMove = false; //the move cant be made
            }
        } else if ((Math.abs(startX - newX) == 1) && (((startY - newY) == 1))) {
            if (piecePresent(e.getX(), e.getY())) {
                if (checkBlackOponent(e.getX(), e.getY())) {
                    validMove = true;
                    if (newY == 0) {
                        success = true;
                    }
                } else {
                    validMove = false;
                }
            } else {
                validMove = false;
            }
        } else if ((startY != 6) && ((startX == newX) && (((startY - newY) == 1)))) { //if the black pawn gets to the other side of the board it becomes a queen
            //If there is a piece in the way
            if (!piecePresent(e.getX(), e.getY())) {
                validMove = true;
                if (newY == 0) {
                    success = true;
                }
            } else {
                validMove = false;
            }
        } else {
            validMove = false;
        }
    }//end blackpawn method

    private void RookMethod(MouseEvent e) { //rook method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);

        int j = (startX - newX) + (startY - newY); //j is the starting position of x minus the new position, the change in X is j. Can be negative or positive because rook can move up or down.
        if (j < 0) {
            j = j * -1; //make sure in integer is always positive to use in the for loop as for loop cant run on negative number
        }

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        for (int i = 1; i <= j; i++) {
            if ((startY == newY) && (startX > newX)) {
                if (piecePresent(((startX - i) * 75), ((startY) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteRook") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackRook") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//end if for going left
            //moving right
            if ((startY == newY) && (startX < newX)) {
                if (piecePresent(((startX + i) * 75), ((startY) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteRook") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackRook") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//end if for moving to the right
            //moving down on the board
            if ((startY < newY) && (startX == newX)) {
                if (piecePresent(((startX) * 75), ((startY + i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteRook") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackRook") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//end if for moving down on the board
            //moving up on the board
            if ((startY > newY) && (startX == newX)) {
                if (piecePresent(((startX) * 75), ((startY - i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteRook") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackRook") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//end if for moving up on the board
        }//end for loop
    }//end rook method

    private void KnightMethod(MouseEvent e) {//knight method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if (((newX * 75 == (startX * 75) + 75) && (newY * 75 == (startY * 75) + 150))//move down two spaces on the y axis and one space to the right on the x axis
                || ((newX * 75 == (startX * 75) - 75) && (newY * 75 == (startY * 75) + 150))//move down two spaces on the y axis and one space to the left on the x axis
                || ((newX * 75 == (startX * 75) + 150) && (newY * 75 == (startY * 75) + 75))//move down one spaces on the y axis and two spaces to the right on the x axis
                || ((newX * 75 == (startX * 75) - 150) && (newY * 75 == (startY * 75) + 75))//move down one spaces on the y axis and two spaces to the left on the x axis
                || ((newX * 75 == (startX * 75) + 75) && (newY * 75 == (startY * 75) - 150))//move up two spaces on the y axis and one space to the right on the x axis
                || ((newX * 75 == (startX * 75) - 75) && (newY * 75 == (startY * 75) - 150))//move up two spaces on the y axis and one space to the left on the x axis
                || ((newX * 75 == (startX * 75) + 150) && (newY * 75 == (startY * 75) - 75))//move up one space on the y axis and two spaces to the right on the x axis
                || ((newX * 75 == (startX * 75) - 150) && (newY * 75 == (startY * 75) - 75))) {//move up one space on the y axis and two spaces to the left on the x axis
            if (!piecePresent(e.getX(), (e.getY()))) {
                validMove = true;
            } else {
                if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    validMove = false;
                } else if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    validMove = false;
                } else {
                    validMove = true;
                }
            }//end else
        } else {
            validMove = false;
        }
    }//end knight method

    private void BishopMethod(MouseEvent e) {//bishop method
        int newX = (e.getX() / 75); //position of piece after moved on X axis
        int newY = (e.getY() / 75); //position of piece after moved on Y axis

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }


        int j = (startX - newX); //j is the starting position of x minus the new position, the change in X is j. Can be negative or positive because bishop can move up or down diagonally
        if (j < 0) {
            j = j * -1; //make sure in integer is always positive to use in the for loop as for loop cant run on negative number
        }
        //convert int values to strings
        String str1 = Integer.toString(newX);
        String str2 = Integer.toString(newY);
        String str3 = Integer.toString(startX);
        String str4 = Integer.toString(startY);

        //add the converted strings together and parse them back into an integer do a modulus
        int value1 = Integer.parseInt(str1 + str2);
        int value2 = Integer.parseInt(str3 + str4);
        int value = value1 - value2; //minus the two parsed integers to find the value that we divide by (9 and 11)


        for (int i = 1; i <= j; i++) { //loop through all the moves possible

            //move down and left. if the new value stored after value1 - value2 is divisible by 9, there is a valid move
            if ((value % 9 == 0) && (startX > newX)) { //if the remainder of value when divided by 9 is equal to 0
                if (piecePresent(((startX - i) * 75), ((startY + i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteBishup") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackBishup") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//END IF FOR modulus

            //move up and to the left. if the new value stored after value1 - value2 is divisible by 11, there is a valid move
            if ((value % 11 == 0) && (startX > newX)) {
                if (piecePresent(((startX - i) * 75), ((startY - i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteBishup") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackBishup") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//END IF FOR modulus

            //move down and to the right
            if ((value % 11 == 0) && (startX < newX)) {
                if (piecePresent(((startX + i) * 75), ((startY + i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteBishup") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackBishup") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//END IF FOR modulus

            //move up and to the right
            if ((value % 9 == 0) && (startX < newX)) {
                if (piecePresent(((startX + i) * 75), ((startY - i) * 75))) {
                    if ((j == i) && (checkWhiteOponent((newX * 75), (newY * 75))) && (pieceName.equals("WhiteBishup") || pieceName.equals("WhiteQueen"))) {
                        validMove = true;
                        break;
                    } else if ((j == i) && (checkBlackOponent((newX * 75), (newY * 75))) && (pieceName.equals("BlackBishup") || pieceName.equals("BlackQueen"))) {
                        validMove = true;
                        break;
                    }//end else if
                    else {
                        validMove = false;
                        break;
                    }
                }//end if piece name
                else {
                    validMove = true;
                }
            }//END IF FOR modulus

        }//end for for J

    }//end bishop method

    private void QueenMethod(MouseEvent e) {//queen method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if ((pieceName.equals("WhiteQueen")) || (pieceName.equals("BlackQueen"))) { //if the white queen or black queen are trying to move
            RookMethod(e); //call the rook method to allow the queen to move up, down , left and right like the rook
            if (!validMove) { //if this move is not valid
                BishopMethod(e); //call the bishop method and allow the queen to move diagonally like the bishop
            }
        }


    }//end queen method

    private void KingMethod(MouseEvent e) {//king method
        int newX = (e.getX() / 75);
        int newY = (e.getY() / 75);

        if (e.getX() > 590 || e.getX() < 0 || e.getY() < 0 || e.getY() > 590) {
            validMove = false;
            return;
        }
        if ((startX * 75 == newX * 75 && (startY * 75) + 75 == newY * 75) //move one space down on the y axis
                || ((startX * 75) + 75 == newX * 75 && startY * 75 == newY * 75)//move one space to the right on the x axis
                || (startX * 75 == newX * 75 && (startY * 75) - 75 == newY * 75)//move one space up on the y axis
                || ((startX * 75) - 75 == newX * 75 && startY * 75 == newY * 75)//move one space to the left on the x axis
                || ((startX * 75) + 75 == newX * 75 && (startY * 75) - 75 == newY * 75)//diagonal move, one space up on the y axis and one space right on the x axis
                || ((startX * 75) - 75 == newX * 75 && (startY * 75) + 75 == newY * 75)//diagonal move, one space down on the y axis and one space left on the x axis
                || ((startX * 75) + 75 == newX * 75 && (startY * 75) + 75 == newY * 75)//diagonal move, one space down on the y axis and one space right on the x axis
                || ((startX * 75) - 75 == newX * 75 && (startY * 75) - 75 == newY * 75)) //diagonal move, one space up on the y axis and one space left on the x axis
        {

            if (!piecePresent(e.getX(), (e.getY()))) {
                validMove = true;
            } else {
                if ((!checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("White"))) {
                    validMove = false;
                } else if ((checkWhiteOponent(e.getX(), e.getY())) && (pieceName.contains("Black"))) {
                    validMove = false;
                } else {
                    validMove = true;
                }
            }//end else
        }//end else for the king to move in any direction
        else {
            validMove = false;
        }

    }//end king method

    //************************************************************************
    // AI code starts from here down....
    //*************************************************************************

    //valid movements the black pawn can attack
    private Stack getWhitePawnSquares(int x, int y, String piece) {
        Stack moves = new Stack();
        Square startingSquare = new Square(x, y, piece);
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y + 2;

        if (y == 1) { //if the starting position of the white pawn is on row 1
            Move validM;
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) {
                Square tmp = new Square(x, tmpy1, newPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && !piecePresent((x * 75) + 20, (tmpy2 * 75) + 20)) {
                Square tmp = new Square(x, tmpy2, newPiece(x, tmpy2));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, newPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }
        } else { //if the white pawn is no longer on the starting position
            Move validM2;
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) {
                Square tmp = new Square(x, tmpy1, newPiece(x, tmpy1));
                validM2 = new Move(startingSquare, tmp);
                moves.push(validM2);
            }
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (!checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
                    validM2 = new Move(startingSquare, tmp);
                    moves.push(validM2);
                }
            }
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (!checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, newPiece(tmpx2, tmpy1));
                    validM2 = new Move(startingSquare, tmp);
                    moves.push(validM2);
                }
            }
        }

        return moves;
    }


    //valid movements the black pawn can attack
    private Stack getBlackPawnSquares(int x, int y, String piece) {
        Stack moves = new Stack();
        Square startingSquare = new Square(x, y, piece);
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y - 1;
        int tmpy2 = y - 2;

        if (y == 6) {// if the starting position of the black rook is on row 6
            Move validM;
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) { //if no piece infront of the pawn allow a movement
                Square tmp = new Square(x, tmpy1, newPiece(x, tmpy1));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && !piecePresent((x * 75) + 20, (tmpy2 * 75) + 20)) { //if no piece two squares in front of pawn allow movement
                Square tmp = new Square(x, tmpy2, newPiece(x, tmpy2));
                validM = new Move(startingSquare, tmp);
                moves.push(validM);
            }
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkBlackOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) { //if  piece in front and to the right of pawn and it is an enemy allow it to take it
                    Square tmp = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {
                if (checkBlackOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {//if piece in front and to the left of pawn and it is an enemy allow it to take it
                    Square tmp = new Square(tmpx2, tmpy1, newPiece(tmpx2, tmpy1));
                    validM = new Move(startingSquare, tmp);
                    moves.push(validM);
                }
            }
        } else { // if the pawn is no longer in the starting position
            Move validM2;
            if (!piecePresent((x * 75) + 20, (tmpy1 * 75) + 20) && tmpy1 >= 0 && tmpy1 <= 7) { //allow the pawn to move move forward once
                Square tmp = new Square(x, tmpy1, newPiece(x, tmpy1));
                validM2 = new Move(startingSquare, tmp);
                moves.push(validM2);
            }
            if (piecePresent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20) && tmpx1 >= 0 && tmpx1 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {//if piece in front and to the right of pawn and it is an enemy allow it to take it
                if (!checkWhiteOponent((tmpx1 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
                    validM2 = new Move(startingSquare, tmp);
                    moves.push(validM2);
                }
            }
            if (piecePresent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20) && tmpx2 >= 0 && tmpx2 <= 7 && tmpy1 >= 0 && tmpy1 <= 7) {//if piece in front and to the left of pawn and it is an enemy allow it to take it
                if (!checkWhiteOponent((tmpx2 * 75) + 20, (tmpy1 * 75) + 20)) {
                    Square tmp = new Square(tmpx2, tmpy1, newPiece(tmpx2, tmpy1));
                    validM2 = new Move(startingSquare, tmp);
                    moves.push(validM2);
                }
            }
        }

        return moves;
    }

    //valid movements the rook can attack
    private Stack getRookMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;

        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y;
            if (!(tmpx > 7 || tmpx < 0)) { //allows the rook move right on the board
                Square tmp = new Square(tmpx, tmpy, newPiece(tmpx, tmpy));
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM);
                        break;
                    } else if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int j = 1; j < 8; j++) {
            int tmpx1 = x - j;
            int tmpy1 = y;
            if (!(tmpx1 > 7 || tmpx1 < 0)) { //allows the rook move left on the board
                Square tmp2 = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
                validM2 = new Move(startingSquare, tmp2);
                if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (!checkWhiteOponent(((tmp2.getXC() * 75) + 20), ((tmp2.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM2);
                        break;
                    } else if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), ((tmp2.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int k = 1; k < 8; k++) {
            int tmpx3 = x;
            int tmpy3 = y + k;
            if (!(tmpy3 > 7 || tmpy3 < 0)) { //allows the rook move down on the board
                Square tmp3 = new Square(tmpx3, tmpy3, newPiece(tmpx3, tmpy3));
                validM3 = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (!checkWhiteOponent(((tmp3.getXC() * 75) + 20), ((tmp3.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM3);
                        break;
                    } else if (checkWhiteOponent(((tmp3.getXC() * 75) + 20), ((tmp3.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        for (int l = 1; l < 8; l++) {
            int tmpx4 = x;
            int tmpy4 = y - l;
            if (!(tmpy4 > 7 || tmpy4 < 0)) { //allows the rook move up on the board
                Square tmp4 = new Square(tmpx4, tmpy4, newPiece(tmpx4, tmpy4));
                validM4 = new Move(startingSquare, tmp4);
                if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if (!checkWhiteOponent(((tmp4.getXC() * 75) + 20), ((tmp4.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM4);
                        break;
                    } else if (checkWhiteOponent(((tmp4.getXC() * 75) + 20), ((tmp4.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }//end of the loop with x increasing and Y doing nothing...
        return moves;
    }// end of get Rook Moves.

    //valid movements the knight can attack
    private Stack getKnightMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Stack attackingMove = new Stack();
        Square s = new Square(x + 1, y + 2, newPiece(x + 1, y + 2));
        moves.push(s);
        Square s1 = new Square(x + 1, y - 2, newPiece(x + 1, y - 2));
        moves.push(s1);
        Square s2 = new Square(x - 1, y + 2, newPiece(x - 1, y + 2));
        moves.push(s2);
        Square s3 = new Square(x - 1, y - 2, newPiece(x - 1, y - 2));
        moves.push(s3);
        Square s4 = new Square(x + 2, y + 1, newPiece(x + 2, y + 1));
        moves.push(s4);
        Square s5 = new Square(x + 2, y - 1, newPiece(x + 2, y - 1));
        moves.push(s5);
        Square s6 = new Square(x - 2, y + 1, newPiece(x - 2, y + 1));
        moves.push(s6);
        Square s7 = new Square(x - 2, y - 1, newPiece(x - 2, y - 1));
        moves.push(s7);

        for (int i = 0; i < 8; i++) {
            Square tmp = (Square) moves.pop();
            Move tmpmove = new Move(startingSquare, tmp);
            if ((tmp.getXC() < 0) || (tmp.getXC() > 7) || (tmp.getYC() < 0) || (tmp.getYC() > 7)) {

            } else if (piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                    attackingMove.push(tmpmove);
                } else if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("White"))) {
                    attackingMove.push(tmpmove);
                }
            } else {
                attackingMove.push(tmpmove);
            }
        }
        return attackingMove;
    }

    //valid movements the bishop can attack
    private Stack getBishopMoves(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;

        for (int i = 1; i < 8; i++) {
            int tmpx = x + i;
            int tmpy = y + i;
            if (!(tmpx > 7 || tmpx < 0 || tmpy > 7 || tmpy < 0)) {
                Square tmp = new Square(tmpx, tmpy, newPiece(tmpx, tmpy));
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM);
                        break;
                    } else if (checkWhiteOponent(((tmp.getXC() * 75) + 20), ((tmp.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } // end of the first for Loop
        for (int k = 1; k < 8; k++) {
            int tmpk = x + k;
            int tmpy2 = y - k;
            if (!(tmpk > 7 || tmpk < 0 || tmpy2 > 7 || tmpy2 < 0)) {
                Square tmpK1 = new Square(tmpk, tmpy2, newPiece(tmpk, tmpy2));
                validM2 = new Move(startingSquare, tmpK1);
                if (!piecePresent(((tmpK1.getXC() * 75) + 20), (((tmpK1.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (!checkWhiteOponent(((tmpK1.getXC() * 75) + 20), ((tmpK1.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM2);
                        break;
                    } else if (checkWhiteOponent(((tmpK1.getXC() * 75) + 20), ((tmpK1.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM2);
                        break;
                    } else {
                        break;
                    }
                }
            }
        } //end of second loop.
        for (int l = 1; l < 8; l++) {
            int tmpL2 = x - l;
            int tmpy3 = y + l;
            if (!(tmpL2 > 7 || tmpL2 < 0 || tmpy3 > 7 || tmpy3 < 0)) {
                Square tmpLMov2 = new Square(tmpL2, tmpy3, newPiece(tmpL2, tmpy3));
                validM3 = new Move(startingSquare, tmpLMov2);
                if (!piecePresent(((tmpLMov2.getXC() * 75) + 20), (((tmpLMov2.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (!checkWhiteOponent(((tmpLMov2.getXC() * 75) + 20), ((tmpLMov2.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM3);
                        break;
                    } else if (checkWhiteOponent(((tmpLMov2.getXC() * 75) + 20), ((tmpLMov2.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM3);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the third loop
        for (int n = 1; n < 8; n++) {
            int tmpN2 = x - n;
            int tmpy4 = y - n;
            if (!(tmpN2 > 7 || tmpN2 < 0 || tmpy4 > 7 || tmpy4 < 0)) {
                Square tmpNmov2 = new Square(tmpN2, tmpy4, newPiece(tmpN2, tmpy4));
                validM4 = new Move(startingSquare, tmpNmov2);
                if (!piecePresent(((tmpNmov2.getXC() * 75) + 20), (((tmpNmov2.getYC() * 75) + 20)))) {
                    moves.push(validM4);
                } else {
                    if (!checkWhiteOponent(((tmpNmov2.getXC() * 75) + 20), ((tmpNmov2.getYC() * 75) + 20)) && (piece.contains("Black"))) {
                        moves.push(validM4);
                        break;
                    } else if (checkWhiteOponent(((tmpNmov2.getXC() * 75) + 20), ((tmpNmov2.getYC() * 75) + 20)) && (piece.contains("White"))) {
                        moves.push(validM4);
                        break;
                    } else {
                        break;
                    }
                }
            }
        }// end of the last loop
        return moves;
    }//end bishop move method

    //valid movements the queen can attack
    private Stack getQueenMoves(int x, int y, String piece) {
        Stack completeMoves = new Stack();
        Stack tmpMoves = new Stack();
        Move tmp;
        tmpMoves = getRookMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        tmpMoves = getBishopMoves(x, y, piece);
        while (!tmpMoves.empty()) {
            tmp = (Move) tmpMoves.pop();
            completeMoves.push(tmp);
        }
        return completeMoves;
    }//end getQueenMoves

    //getter for piece name used in checkSurroundingSquares
    public String getPieceName(int x, int y) {
        return pieceName;
    }

    private Boolean checkSurroundingSquares(Square s) {
        Boolean possible = false;
        int x = s.getXC() * 75;
        int y = s.getYC() * 75;
        if (!((getPieceName((x + 75), y).contains("BlackKing")) // if there is no black king to the right of x,y
                || (getPieceName((x - 75), y).contains("BlackKing")) //if there is no black king to the left of x,y
                || (getPieceName(x, (y + 75)).contains("BlackKing"))//if there is no black king below x,y
                || (getPieceName((x), (y - 75)).contains("BlackKing"))//if there is no black king above x,y
                || (getPieceName((x + 75), (y + 75)).contains("BlackKing"))//if there is no black king diagonally downwards to the right from x,y
                || (getPieceName((x - 75), (y + 75)).contains("BlackKing"))//if there is no black king diagonally downwards to the left from x,y
                || (getPieceName((x + 75), (y - 75)).contains("BlackKing"))//if there is no black king diagonally upwards to the right from x,y
                || (getPieceName((x - 75), (y - 75)).contains("BlackKing")))) { //if there is no black king diagonally upwards to the left from x,y
            possible = true; //then a possible move can be made
        }
        return possible;
    }


    //valid movements the king can attack
    private Stack getKingSquares(int x, int y, String piece) {
        Square startingSquare = new Square(x, y, piece);
        Stack moves = new Stack();
        Move validM, validM2, validM3, validM4;
        int tmpx1 = x + 1;
        int tmpx2 = x - 1;
        int tmpy1 = y + 1;
        int tmpy2 = y - 1;

        //if the kings movement to the left is not greater than 7 allow the piece to be moved, this prevents it from falling off the board
        if (!((tmpx1 > 7))) {//movement to the right

            Square tmp = new Square(tmpx1, y, newPiece(tmpx1, y));
            Square tmp1 = new Square(tmpx1, tmpy1, newPiece(tmpx1, tmpy1));
            Square tmp2 = new Square(tmpx1, tmpy2, newPiece(tmpx1, tmpy2));
            if (checkSurroundingSquares(tmp)) {// check the surrounding squares
                validM = new Move(startingSquare, tmp);
                if (!piecePresent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (!checkWhiteOponent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                        moves.push(validM);
                    } else if (checkWhiteOponent(((tmp.getXC() * 75) + 20), (((tmp.getYC() * 75) + 20))) && (piece.contains("White"))) {
                        moves.push(validM);
                    }
                }
            }
            if (!(tmpy1 > 7)) { //kings movement down and to the right
                if (checkSurroundingSquares(tmp1)) {
                    validM2 = new Move(startingSquare, tmp1);
                    if (!piecePresent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (!checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                            moves.push(validM2);
                        } else if (checkWhiteOponent(((tmp1.getXC() * 75) + 20), (((tmp1.getYC() * 75) + 20))) && (piece.contains("White"))) {
                            moves.push(validM2);
                        }

                    }
                }
            }
            if (!(tmpy2 < 0)) {//king movement up and to the right
                if (checkSurroundingSquares(tmp2)) {
                    validM3 = new Move(startingSquare, tmp2);
                    if (!piecePresent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        System.out.println("The values that we are going to be looking at are : " + ((tmp2.getXC() * 75) + 20) + " and the y value is : " + ((tmp2.getYC() * 75) + 20));
                        if (!checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                            moves.push(validM3);
                        } else if (checkWhiteOponent(((tmp2.getXC() * 75) + 20), (((tmp2.getYC() * 75) + 20))) && (piece.contains("White"))) {
                            moves.push(validM3);
                        }

                    }
                }
            }
        }
        if (!((tmpx2 < 0))) { //movement to the left
            Square tmp3 = new Square(tmpx2, y, newPiece(tmpx2, y));
            Square tmp4 = new Square(tmpx2, tmpy1, newPiece(tmpx2, tmpy1));
            Square tmp5 = new Square(tmpx2, tmpy2, newPiece(tmpx2, tmpy2));
            if (checkSurroundingSquares(tmp3)) {
                validM = new Move(startingSquare, tmp3);
                if (!piecePresent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20)))) {
                    moves.push(validM);
                } else {
                    if (!checkWhiteOponent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                        moves.push(validM);
                    } else if (checkWhiteOponent(((tmp3.getXC() * 75) + 20), (((tmp3.getYC() * 75) + 20))) && (piece.contains("White"))) {
                        moves.push(validM);
                    }

                }
            }
            if (!(tmpy1 > 7)) { //movement down and to the left
                if (checkSurroundingSquares(tmp4)) {
                    validM2 = new Move(startingSquare, tmp4);
                    if (!piecePresent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20)))) {
                        moves.push(validM2);
                    } else {
                        if (!checkWhiteOponent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                            moves.push(validM2);
                        } else if (checkWhiteOponent(((tmp4.getXC() * 75) + 20), (((tmp4.getYC() * 75) + 20))) && (piece.contains("White"))) {
                            moves.push(validM2);
                        }
                    }
                }
            }
            if (!(tmpy2 < 0)) {//movement up and to the right
                if (checkSurroundingSquares(tmp5)) {
                    validM3 = new Move(startingSquare, tmp5);
                    if (!piecePresent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20)))) {
                        moves.push(validM3);
                    } else {
                        if (!checkWhiteOponent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                            moves.push(validM3);
                        } else if (checkWhiteOponent(((tmp5.getXC() * 75) + 20), (((tmp5.getYC() * 75) + 20))) && (piece.contains("White"))) {
                            moves.push(validM3);
                        }
                    }
                }
            }
        }
        Square tmp7 = new Square(x, tmpy1, newPiece(x, tmpy1));
        Square tmp8 = new Square(x, tmpy2, newPiece(x, tmpy2));
        if (!(tmpy1 > 7)) {
            if (checkSurroundingSquares(tmp7)) { //movement down the board
                validM2 = new Move(startingSquare, tmp7);
                if (!piecePresent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20)))) {
                    moves.push(validM2);
                } else {
                    if (!checkWhiteOponent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                        moves.push(validM2);
                    } else if (checkWhiteOponent(((tmp7.getXC() * 75) + 20), (((tmp7.getYC() * 75) + 20))) && (piece.contains("White"))) {
                        moves.push(validM2);
                    }

                }
            }
        }
        if (!(tmpy2 < 0)) {
            if (checkSurroundingSquares(tmp8)) { //movement up the board
                validM3 = new Move(startingSquare, tmp8);
                if (!piecePresent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20)))) {
                    moves.push(validM3);
                } else {
                    if (!checkWhiteOponent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20))) && (piece.contains("Black"))) {
                        moves.push(validM3);
                    } else if (checkWhiteOponent(((tmp8.getXC() * 75) + 20), (((tmp8.getYC() * 75) + 20))) && (piece.contains("White"))) {
                        moves.push(validM3);
                    }

                }
            }
        }
        return moves;
    } // end of the method getKingSquares

    /*
        Method to colour a stack of Squares
    */
    private void colorSquares(Stack squares) {
        Border greenBorder = BorderFactory.createLineBorder(Color.GREEN, 3);
        while (!squares.empty()) {
            Square s = (Square) squares.pop();
            int location = s.getXC() + ((s.getYC()) * 8);
            JPanel panel = (JPanel) chessBoard.getComponent(location);
            panel.setBorder(greenBorder);
        }
    }

    /*
        Method to get the landing square of a bunch of moves
    */
    private void getLandingSquares(Stack found) {
        Move tmp;
        Square landing;
        Stack squares = new Stack();
        while (!found.empty()) {
            tmp = (Move) found.pop();
            landing = (Square) tmp.getLanding();
            squares.push(landing);
        }
        colorSquares(squares);
    }

    //method to find the location of all white pieces on the board
    public Stack findWhitePieces() {
        Stack squares = new Stack();
        String icon, pieceName;
        int x, y;

        for (int i = 0; i < 600; i += 75) {
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = chessBoard.findComponentAt(j, i);
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("White")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        return squares;
    }//end findWhitePieces

    //method to find the location of all black pieces on the board
    public Stack findBlackPieces() {
        Stack squares = new Stack();
        String icon, pieceName;
        int x, y;

        for (int i = 0; i < 600; i += 75) {
            for (int j = 0; j < 600; j += 75) {
                y = i / 75;
                x = j / 75;
                Component tmp = chessBoard.findComponentAt(j, i);
                if (tmp instanceof JLabel) {
                    chessPiece = (JLabel) tmp;
                    icon = chessPiece.getIcon().toString();
                    pieceName = icon.substring(0, (icon.length() - 4));
                    if (pieceName.contains("Black")) {
                        Square stmp = new Square(x, y, pieceName);
                        squares.push(stmp);
                    }
                }
            }
        }
        return squares;
    }//end findWhitePieces

    private Stack getWhiteAttackingSquares(Stack pieces) {
        Stack tempK = null;
        while (!pieces.empty()) {
            Square s = (Square) pieces.pop();
            String tmpString = s.getName();
            if (tmpString.contains("Knight")) {
                tempK = getKnightMoves(s.getXC(), s.getYC(), s.getName());
                while (!tempK.empty()) {
                    Square tempKnight = (Square) tempK.pop();
                    tempK.push(tempKnight);
                }
            } else if (tmpString.contains("Bishop")) {

            }
        }
        return tempK;
    }//end getWhiteAttackingSquares

    private void resetBorders() {
        Border empty = BorderFactory.createEmptyBorder();
        for (int i = 0; i < 64; i++) {
            JPanel tmppanel = (JPanel) chessBoard.getComponent(i);
            tmppanel.setBorder(empty);
        }
    }

    /*
        The method printStack takes in a Stack of Moves and prints out all possible moves.
    */
    private void printStack(Stack input) {
        Move m;
        Square s, l;
        while (!input.empty()) {
            m = (Move) input.pop();
            s = (Square) m.getStart();
            l = (Square) m.getLanding();
            System.out.println("The possible move that was found is : (" + s.getXC() + " , " + s.getYC() + "), landing at (" + l.getXC() + " , " + l.getYC() + ")");
        }
    }

    private void makeAIMove() {
    /*
        When the AI Agent decides on a move, a red border shows the square from where the move started and the
        landing square of the move.
    */
        resetBorders();
        layeredPane.validate();
        layeredPane.repaint();
        Stack white = findWhitePieces();
        Stack black = findBlackPieces();
        Stack completeMoves = new Stack();
        Stack completeMoves2 = new Stack();
        Stack temporary;
        Stack temporary2;
        Move tmp;

        //assign the AI to black pieces
        if (AIOption.contains("Random") || (AIOption.contains("NextBest")) || (AIOption.contains("TwoDeep"))) {
            white = black;

            System.out.println("White : " + white.size());
            System.out.println("Black : " + black.size());

            while ((!black.empty())) {
                Square s = (Square) black.pop();
                String tmpString = s.getName();
                Stack tmpMoves = new Stack();

        /*
            identify all the possible black moves that can be made by the AI Opponent
        */
                if (tmpString.contains("Knight")) {
                    tmpMoves = (getKnightMoves(s.getXC(), s.getYC(), s.getName()));
                } else if (tmpString.contains("Bishup")) {
                    tmpMoves = getBishopMoves(s.getXC(), s.getYC(), s.getName());
                } else if ((tmpString.contains("Pawn"))) {
                    tmpMoves = getBlackPawnSquares(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("Rook")) {
                    tmpMoves = getRookMoves(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("Queen")) {
                    tmpMoves = getQueenMoves(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("King")) {
                    tmpMoves = getKingSquares(s.getXC(), s.getYC(), s.getName());
                }

                while (!tmpMoves.empty()) {
                    tmp = (Move) tmpMoves.pop();
                    completeMoves.push(tmp);
                }

            }

            while ((!white.empty())) {
                Square s = (Square) white.pop();
                String tmpString = s.getName();
                Stack tmpMoves = new Stack();

        /*
            identify all the possible white moves that can be made by the AI Opponent
        */
                if (tmpString.contains("Knight")) {
                    tmpMoves = (getKnightMoves(s.getXC(), s.getYC(), s.getName()));
                } else if (tmpString.contains("Bishup")) {
                    tmpMoves = getBishopMoves(s.getXC(), s.getYC(), s.getName());
                } else if ((tmpString.contains("Pawn"))) {
                    tmpMoves = getWhitePawnSquares(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("Rook")) {
                    tmpMoves = getRookMoves(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("Queen")) {
                    tmpMoves = getQueenMoves(s.getXC(), s.getYC(), s.getName());
                } else if (tmpString.contains("King")) {
                    tmpMoves = getKingSquares(s.getXC(), s.getYC(), s.getName());
                }

                while (!tmpMoves.empty()) {
                    tmp = (Move) tmpMoves.pop();
                    completeMoves2.push(tmp);
                }

            }
        }


        temporary = (Stack) completeMoves.clone();
        temporary2 = (Stack) completeMoves2.clone();

        getLandingSquares(temporary);

        printStack(temporary);
    /*
        So now we should have a copy of all the possible moves to make in our Stack called completeMoves
    */
        if (completeMoves.size() == 0) {
    /*
        In Chess if you cannot make a valid move but you are not in Check this state is referred to
        as a Stale Mate
    */
            JOptionPane.showMessageDialog(null, "Congratulations, you have placed the AI component in a Stale Mate Position");
            System.exit(0);

        } else {
    /*
        Okay, so we can make a move now. We have a stack of all possible moves and need to call the correct agent to select
        one of these moves. Lets print out the possible moves to the standard output to view what the options are for
        White. Later when you are finished the continuous assessment you don't need to have such information being printed
        out to the standard output.
     */
            // System.out.println("=============================================================");
            Stack testing = new Stack();
            Stack testing2 = new Stack();
            while (!completeMoves.empty()) {
                Move tmpMove = (Move) completeMoves.pop();
                Square s1 = (Square) tmpMove.getStart();
                Square s2 = (Square) tmpMove.getLanding();
                // System.out.println("The " + s1.getName() + " can move from (" + s1.getXC() + ", " + s1.getYC() + ") to the following square: (" + s2.getXC() + ", " + s2.getYC() + ")");
                testing.push(tmpMove);
            }
            while (!completeMoves2.empty()) {
                Move tmpMove = (Move) completeMoves2.pop();
                Square s1 = (Square) tmpMove.getStart();
                Square s2 = (Square) tmpMove.getLanding();
                // System.out.println("The " + s1.getName() + " can move from (" + s1.getXC() + ", " + s1.getYC() + ") to the following square: (" + s2.getXC() + ", " + s2.getYC() + ")");
                testing2.push(tmpMove);
            }
            // System.out.println("=============================================================");
            Border redBorder = BorderFactory.createLineBorder(Color.RED, 3);
            Move selectedMove = null;
            if ((AIOption.contains("Random"))) {
                selectedMove = agent.randomMove(testing);
            } else if ((AIOption.contains("NextBest"))) {
                selectedMove = agent.nextBestMove(testing);
            } else if (AIOption.contains("TwoDeep")) {
                selectedMove = agent.twoLevelsDeep(testing, testing2);
            }

            Square startingPoint = (Square) selectedMove.getStart();
            Square landingPoint = (Square) selectedMove.getLanding();
            int startX1 = (startingPoint.getXC() * 75) + 20;
            int startY1 = (startingPoint.getYC() * 75) + 20;
            int landingX1 = (landingPoint.getXC() * 75) + 20;
            int landingY1 = (landingPoint.getYC() * 75) + 20;
            // System.out.println("-------- Move " + startingPoint.getName() + " (" + startingPoint.getXC() + ", " + startingPoint.getYC() + ") to (" + landingPoint.getXC() + ", " + landingPoint.getYC() + ")");

            Component c = (JLabel) chessBoard.findComponentAt(startX1, startY1);
            Container parent = c.getParent();
            parent.remove(c);
            int panelID = (startingPoint.getYC() * 8) + startingPoint.getXC();
            panels = (JPanel) chessBoard.getComponent(panelID);
            panels.setBorder(redBorder);
            parent.validate();

            Component l = chessBoard.findComponentAt(landingX1, landingY1);
            if (l instanceof JLabel) {
                Container parentlanding = l.getParent();
                JLabel awaitingName = (JLabel) l;
                String agentCaptured = awaitingName.getIcon().toString();
                boolean agentwins = false;

                if (agentCaptured.contains("WhiteKing")) {
                    agentwins = true;
                }



                parentlanding.remove(l);
                parentlanding.validate();
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
                if (agentwins) {

                    JOptionPane.showMessageDialog(null, "The AI Agent has won!");
                    System.exit(0);
                }

            } else {
                pieces = new JLabel(new ImageIcon(startingPoint.getName() + ".png"));
                int landingPanelID = (landingPoint.getYC() * 8) + landingPoint.getXC();
                panels = (JPanel) chessBoard.getComponent(landingPanelID);
                panels.add(pieces);
                panels.setBorder(redBorder);
                layeredPane.validate();
                layeredPane.repaint();
            }

        }

    }//end


    public void mouseClicked(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public static void showBoard() {
        JFrame frame = new ChessProject();
        frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void pickAI() { //method to choose which AI you want to play against
        JFrame pickAI = new JFrame("Pick your AI agent");
        pickAI.setLayout(new FlowLayout());
        Dimension sizeAI = new Dimension(600, 80);
        pickAI.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pickAI.setSize(sizeAI);
        pickAI.setVisible(true);
        pickAI.setLocation(700, 400);
        pickAI.setResizable(true);


        JButton randomAI = new JButton("Random Move AI");
        pickAI.add(randomAI);
        randomAI.setVisible(true);
        randomAI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pickAI.setVisible(false);
                AIOption = "Random";
                showBoard();

            }
        });

        JButton nextBestAi = new JButton("Next best AI");
        pickAI.add(nextBestAi);
        nextBestAi.setVisible(true);
        nextBestAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pickAI.setVisible(false);
                AIOption = "NextBest";
                showBoard();
            }
        });

        JButton twoDeepAi = new JButton("Two Deep AI");
        pickAI.add(twoDeepAi);
        twoDeepAi.setVisible(true);
        twoDeepAi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pickAI.setVisible(false);
                AIOption = "TwoDeep";
                showBoard();
            }
        });

    }


    /*
        Main method that gets the ball moving.
    */
    public static void main(String[] args) {
        pickAI();
    }
}


