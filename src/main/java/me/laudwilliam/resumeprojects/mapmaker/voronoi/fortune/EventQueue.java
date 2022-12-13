package me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune;

import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.CircleEvent;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.Event;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.events.SiteEvent;
import me.laudwilliam.resumeprojects.mapmaker.voronoi.fortune.geometry.Site;

class EventQueue {
    private int size;
    private Node head;

    public void addEvent(double[] eventInfo) {

    }

    private Node add(Event event) {
        Node node = new Node(event);
        size++;
        if (head == null)
            return head = node;
        if (head.event.compareTo(event) < 0) {
            Node oldHead = head;
            Node prev = head.previous;
            head = node;
            node.next = oldHead;
            node.previous = prev;
            oldHead.previous = node;

            if (prev != null) {
                prev.next = node;
            }
            return node;
        }

        Node current = head;
        while (current.event.compareTo(event) > -1) {
            if (current.next == null)
                break;
            current = current.next;
        }
        Node next = current.next;
        current.next = node;
        node.previous = current;
        node.next = next;

        if (next != null) {
            next.previous = node;
        }
        return node;
    }

    // Public
    public Event addEvent(Site site) {
        Event event = new SiteEvent(site);
        add(event);
        return event;
    }

    public void addEvent(CircleEvent event) {
        add(event);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Event pop() {
        if (size == 0)
            return null;
        size--;
        Node node = head;
        head = head.next;
        return node.event;
    }

    // Private
    private class Node {
        final Event event;
        Node next;
        Node previous;

        private Node(Event event) {
            this.event = event;
        }
    }


}
