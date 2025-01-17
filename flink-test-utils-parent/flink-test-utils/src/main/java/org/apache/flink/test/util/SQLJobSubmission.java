/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.test.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.apache.flink.util.Preconditions.checkNotNull;

/** Programmatic definition of a SQL job-submission. */
public class SQLJobSubmission {

    private final ClientMode clientMode;
    private final List<String> sqlLines;
    private final List<String> jars;

    private SQLJobSubmission(ClientMode clientMode, List<String> sqlLines, List<String> jars) {
        this.clientMode = clientMode;
        this.sqlLines = checkNotNull(sqlLines);
        this.jars = checkNotNull(jars);
    }

    public ClientMode getClientMode() {
        return clientMode;
    }

    public List<String> getJars() {
        return this.jars;
    }

    public List<String> getSqlLines() {
        return this.sqlLines;
    }

    /** Builder for the {@link SQLJobSubmission}. */
    public static class SQLJobSubmissionBuilder {
        private ClientMode clientMode = ClientMode.SQL_CLIENT;
        private final List<String> sqlLines;
        private final List<String> jars = new ArrayList<>();

        public SQLJobSubmissionBuilder(List<String> sqlLines) {
            this.sqlLines = sqlLines;
        }

        public SQLJobSubmissionBuilder setClientMode(ClientMode clientMode) {
            this.clientMode = clientMode;
            return this;
        }

        public SQLJobSubmissionBuilder addJar(Path jarFile) {
            this.jars.add(jarFile.toAbsolutePath().toString());
            return this;
        }

        public SQLJobSubmissionBuilder addJars(Path... jarFiles) {
            for (Path jarFile : jarFiles) {
                addJar(jarFile);
            }
            return this;
        }

        public SQLJobSubmissionBuilder addJars(List<Path> jarFiles) {
            jarFiles.forEach(this::addJar);
            return this;
        }

        public SQLJobSubmission build() {
            return new SQLJobSubmission(clientMode, sqlLines, jars);
        }
    }

    /** Use which client to submit job. */
    public enum ClientMode {
        SQL_CLIENT,

        HIVE_JDBC
    }
}
