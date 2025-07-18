/*
 * Copyright 2024 the original author or authors.
 * <p>
 * Licensed under the Moderne Source Available License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * https://docs.moderne.io/licensing/moderne-source-available-license
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openrewrite.java.migrate.util;

import org.junit.jupiter.api.Test;
import org.openrewrite.Issue;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;
import static org.openrewrite.java.Assertions.version;

class MigrateCollectionsSingletonMapTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new MigrateCollectionsSingletonMap());
    }

    @Issue("https://github.com/openrewrite/rewrite-migrate-java/issues/72")
    @Test
    void singletonMap() {
        //language=java
        rewriteRun(
          version(
            java(
              """
                import java.util.*;

                class Test {
                    Map<String,String> set = Collections.singletonMap("hello", "world");
                }
                """,
              """
                import java.util.Map;

                class Test {
                    Map<String,String> set = Map.of("hello", "world");
                }
                """
            ),
            9
          )
        );
    }

    @Issue("https://github.com/openrewrite/rewrite-migrate-java/issues/72")
    @Test
    void singletonMapCustomType() {
        //language=java
        rewriteRun(
          version(
            java(
              """
                import java.util.*;
                import java.time.LocalDate;

                class Test {
                    Map<String,LocalDate> map = Collections.singletonMap("date", LocalDate.now());
                }
                """,
              """
                import java.util.Map;
                import java.time.LocalDate;

                class Test {
                    Map<String,LocalDate> map = Map.of("date", LocalDate.now());
                }
                """
            ),
            9
          )
        );
    }

    @Issue("https://github.com/openrewrite/rewrite-migrate-java/issues/571")
    @Test
    void shouldNotConvertLiteralNull() {
        //language=java
        rewriteRun(
          version(
            java(
              """
                import java.util.*;

                class Test {
                    Map<String, String> mapWithNullKey = Collections.singletonMap(null, "foo");
                    Map<String, String> mapWithNullValue = Collections.singletonMap("bar", null);
                }
                """
            ),
            9
          )
        );
    }    
}
