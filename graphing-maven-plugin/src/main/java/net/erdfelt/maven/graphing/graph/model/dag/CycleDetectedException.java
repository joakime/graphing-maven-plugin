package net.erdfelt.maven.graphing.graph.model.dag;

/*
 * Copyright (c) Joakim Erdfelt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.List;

import net.erdfelt.maven.graphing.graph.model.GraphConstraintException;

/**
 * CycleDetectedException
 * 
 * @since 1.0
 */
public class CycleDetectedException extends GraphConstraintException
{
    private static final long serialVersionUID = -7749597681084104681L;
    private List<String> cycle;

    public CycleDetectedException(final String message, final List<String> cycle)
    {
        super(message);

        this.cycle = cycle;
    }

    public List<String> getCycle()
    {
        return cycle;
    }

    /**
     * @return the cycle description
     */
    public String cycleToString()
    {
        final StringBuffer buffer = new StringBuffer();

        boolean needsDelim = false;
        for (String c : cycle)
        {
            if (needsDelim)
            {
                buffer.append(" --> ");
            }
            buffer.append(c);
            needsDelim = true;
        }
        return buffer.toString();
    }

    @Override
    public String getMessage()
    {
        return super.getMessage() + " " + cycleToString();
    }
}
