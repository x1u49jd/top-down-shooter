import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InputHandler implements KeyListener, MouseListener {
    private boolean upPressed, downPressed, leftPressed, rightPressed, startRequested, paused, restartRequested = false;

    private Player player;
    private boolean mousePressed;
    private int mouseX, mouseY;
    
    public InputHandler(Player player) {
        this.player = player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {upPressed = true;};
        if (key == KeyEvent.VK_S) {downPressed = true;};
        if (key == KeyEvent.VK_A) {leftPressed = true;};
        if (key == KeyEvent.VK_D) {rightPressed = true;};
        if (key == KeyEvent.VK_ENTER) {startRequested = true;}
        if (key == KeyEvent.VK_R) {restartRequested = true;}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {upPressed = false;};
        if (key == KeyEvent.VK_S) {downPressed = false;};
        if (key == KeyEvent.VK_A) {leftPressed = false;};
        if (key == KeyEvent.VK_D) {rightPressed = false;};
        if (key == KeyEvent.VK_ENTER) {startRequested = false;}
        if (key == KeyEvent.VK_R) {restartRequested = false;}

        if (key == KeyEvent.VK_P) {paused = !paused;}
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void resetMouse() {
        mousePressed = false;
    }

    public void clearAllInput() {
        mousePressed = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    public void reset() {
        upPressed = false;
        downPressed = false;
        leftPressed = false;
        rightPressed = false;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isStartRequested() {
        return startRequested;
    }

    public boolean isRestartRequested() {
        return restartRequested;
    }

    public boolean isPaused() {
        return paused;
    }
}
