package org.apache.jcs.utils.struct;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This is an basic thread safe single linked list. It provides very limited functionality. It is
 * small and fast.
 * <p>
 * @author Aaron Smuts
 */
public class SingleLinkedList
{
    private static final Log log = LogFactory.getLog( SingleLinkedList.class );

    private Object lock = new Object();

    // the head of the queue
    private Node head = new Node();

    // the end of the queue
    private Node tail = head;

    private int size = 0;

    /**
     * Takes the first item off the list.
     * <p>
     * @return null if the list is empty.
     */
    public Object takeFirst()
    {
        synchronized ( lock )
        {
            // wait until there is something to read
            if ( head == tail )
            {
                return null;
            }

            Node node = head.next;

            Object value = node.payload;

            if ( log.isDebugEnabled() )
            {
                log.debug( "head.payload = " + head.payload );
                log.debug( "node.payload = " + node.payload );
            }

            // Node becomes the new head (head is always empty)

            node.payload = null;
            head = node;

            size--;
            return value;
        }
    }

    /**
     * Adds an item to the end of the list.
     * <p>
     * @param payload
     */
    public void addLast( Object payload )
    {
        Node newNode = new Node();

        newNode.payload = payload;

        synchronized ( lock )
        {
            size++;
            tail.next = newNode;
            tail = newNode;
        }
    }

    /**
     * Removes everything.
     */
    public void clear()
    {
        synchronized ( lock )
        {
            head = tail;
            size = 0;
        }
    }

    /**
     * The list is composed of nodes.
     * <p>
     * @author Aaron Smuts
     */
    private static class Node
    {
        Node next = null;

        Object payload;
    }

    /**
     * Returns the number of elements in the list.
     * <p>
     * @return number of items in the list.
     */
    public int size()
    {
        return size;
    }
}
