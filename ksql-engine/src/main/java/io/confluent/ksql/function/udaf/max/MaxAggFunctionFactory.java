/*
 * Copyright 2018 Confluent Inc.
 *
 * Licensed under the Confluent Community License; you may not use this file
 * except in compliance with the License.  You may obtain a copy of the License at
 *
 * http://www.confluent.io/confluent-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */

package io.confluent.ksql.function.udaf.max;

import io.confluent.ksql.function.AggregateFunctionFactory;
import io.confluent.ksql.function.KsqlAggregateFunction;
import io.confluent.ksql.util.KsqlException;
import java.util.Arrays;
import java.util.List;
import org.apache.kafka.connect.data.Schema;

public class MaxAggFunctionFactory extends AggregateFunctionFactory {
  private static final String FUNCTION_NAME = "MAX";

  public MaxAggFunctionFactory() {
    super(
        FUNCTION_NAME,
        Arrays.asList(
            new DoubleMaxKudaf(FUNCTION_NAME, -1),
            new LongMaxKudaf(FUNCTION_NAME, -1),
            new IntegerMaxKudaf(FUNCTION_NAME, -1)));
  }

  @Override
  public KsqlAggregateFunction getProperAggregateFunction(final List<Schema> argTypeList) {
    // For now we only support aggregate functions with one arg.
    for (final KsqlAggregateFunction<?, ?> ksqlAggregateFunction : getAggregateFunctionList()) {
      if (ksqlAggregateFunction.hasSameArgTypes(argTypeList)) {
        return ksqlAggregateFunction;
      }
    }
    throw new KsqlException("No Max aggregate function with " + argTypeList.get(0) + " "
                            + " argument type exists!");
  }
}
