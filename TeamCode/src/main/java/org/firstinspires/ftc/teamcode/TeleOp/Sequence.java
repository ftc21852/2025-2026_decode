package org.firstinspires.ftc.teamcode.TeleOp;

import java.util.*;

public class Sequence {
    private class Node {
        private int time;
        private Runnable function;
        private Node next;

        public Node(int timeInMs, Runnable function) {
            this.time = timeInMs;
            this.function = function;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private Node currentNode;
    private long timeOfNextState;
    private volatile boolean isRunning;

    public Sequence() {
    }

    public void addState(int timeInMs, Runnable function) {
        if (head == null) {
            head = tail = new Node(timeInMs, function);
        } else {
            tail = tail.next = new Node(timeInMs, function);
        }
    }

    public void start() {
        isRunning = true;
        currentNode = null;
        timeOfNextState = new Date().getTime();
        update();
    }

    public void update() {
        if (!isRunning || currentNode == tail) {
            isRunning = false;
            return;
        }

        if (new Date().getTime() >= timeOfNextState) {
            currentNode = currentNode == null ? head : currentNode.next;
            timeOfNextState += currentNode.time;
            currentNode.function.run();
        }
    }
}
