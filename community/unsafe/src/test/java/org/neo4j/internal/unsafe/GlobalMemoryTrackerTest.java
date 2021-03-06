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
package org.neo4j.internal.unsafe;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalMemoryTrackerTest
{

    @Test
    void trackMemoryAllocations()
    {
        long initialUsedMemory = GlobalMemoryTracker.INSTANCE.usedNativeMemory();
        GlobalMemoryTracker.INSTANCE.allocateNative( 10 );
        GlobalMemoryTracker.INSTANCE.allocateNative( 20 );
        GlobalMemoryTracker.INSTANCE.allocateNative( 40 );
        assertThat( currentlyUsedMemory( initialUsedMemory ) ).isGreaterThanOrEqualTo( 70L );
    }

    @Test
    void trackMemoryDeallocations()
    {
        long initialUsedMemory = GlobalMemoryTracker.INSTANCE.usedNativeMemory();
        GlobalMemoryTracker.INSTANCE.allocateNative( 100 );
        assertThat( currentlyUsedMemory( initialUsedMemory ) ).isGreaterThanOrEqualTo( 100L );

        GlobalMemoryTracker.INSTANCE.releaseNative( 20 );
        assertThat( currentlyUsedMemory( initialUsedMemory ) ).isGreaterThanOrEqualTo( 80L );

        GlobalMemoryTracker.INSTANCE.releaseNative( 40 );
        assertThat( currentlyUsedMemory( initialUsedMemory ) ).isGreaterThanOrEqualTo( 40L );
    }

    private static long currentlyUsedMemory( long initialUsedMemory )
    {
        return GlobalMemoryTracker.INSTANCE.usedNativeMemory() - initialUsedMemory;
    }
}
