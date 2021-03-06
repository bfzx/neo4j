/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.kernel.impl.util.collection;

import org.eclipse.collections.impl.map.mutable.primitive.LongObjectHashMap;

import java.util.Iterator;

import org.neo4j.memory.MemoryTracker;
import org.neo4j.memory.ScopedMemoryTracker;

import static java.util.Collections.emptyIterator;
import static org.neo4j.memory.HeapEstimator.shallowSizeOfInstance;

public class LongProbeTable<V> implements AutoCloseable
{
    private static final long SHALLOW_SIZE = shallowSizeOfInstance( LongProbeTable.class );
    static final long SCOPED_MEMORY_TRACKER_SHALLOW_SIZE = shallowSizeOfInstance( ScopedMemoryTracker.class );
    private final ScopedMemoryTracker scopedMemoryTracker;
    private final LongObjectHashMap<HeapTrackingAppendList<V>> map;

    public static <V> LongProbeTable<V> createLongProbeTable( MemoryTracker memoryTracker )
    {
        ScopedMemoryTracker scopedMemoryTracker = new ScopedMemoryTracker( memoryTracker );
        scopedMemoryTracker.allocateHeap( SHALLOW_SIZE + SCOPED_MEMORY_TRACKER_SHALLOW_SIZE );
        return new LongProbeTable<>( scopedMemoryTracker );
    }

    private LongProbeTable( ScopedMemoryTracker scopedMemoryTracker )
    {
        this.scopedMemoryTracker = scopedMemoryTracker;
        this.map = HeapTrackingLongObjectHashMap.createLongObjectHashMap( scopedMemoryTracker );
    }

    public void put( long key, V value )
    {
        map.getIfAbsentPutWith( key, HeapTrackingAppendList::newAppendList, scopedMemoryTracker ).add( value );
    }

    public Iterator<V> get( long key )
    {
        var entry = map.get( key );
        if ( entry == null )
        {
            return emptyIterator();
        }
        return entry.iterator();
    }

    @Override
    public void close()
    {
        scopedMemoryTracker.close();
    }
}
