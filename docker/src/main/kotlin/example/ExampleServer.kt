/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example

import google.example.RspApplicationGrpc
import google.example.GRequest
import google.example.GResponse
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import io.grpc.protobuf.services.ProtoReflectionService
import java.io.IOException

/**
 * Example server.
 *
 * Adapted from the gRPC examples:
 * https://github.com/grpc/grpc-java/blob/master/examples/example-kotlin
 */
class ExampleServer(private val port: Int = 8080) {

    companion object {
        /** Main launches the server from the command line. */
        @JvmStatic
        @Throws(IOException::class, InterruptedException::class)
        fun main(args: Array<String>) {
            val server = RspServer()
            server.start()
            server.blockUntilShutdown()
        }
    }
    
}
