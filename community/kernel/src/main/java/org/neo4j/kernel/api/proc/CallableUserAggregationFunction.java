/*
 * Copyright (c) 2002-2018 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
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
package org.neo4j.kernel.api.proc;

import org.neo4j.internal.kernel.api.exceptions.ProcedureException;
import org.neo4j.internal.kernel.api.procs.UserAggregator;
import org.neo4j.internal.kernel.api.procs.UserFunctionSignature;

public interface CallableUserAggregationFunction
{
    UserFunctionSignature signature();
    UserAggregator create( Context ctx ) throws ProcedureException;

    abstract class BasicUserAggregationFunction implements CallableUserAggregationFunction
    {
        private final UserFunctionSignature signature;

        protected BasicUserAggregationFunction( UserFunctionSignature signature )
        {
            this.signature = signature;
        }

        @Override
        public UserFunctionSignature signature()
        {
            return signature;
        }

        @Override
        public abstract UserAggregator create( Context ctx ) throws ProcedureException;
    }
}
