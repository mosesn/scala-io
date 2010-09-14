/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2009-2010, Jesse Eichar             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package scalaio.test.fs

import scalax.io._
import scalax.io.resource._

import Path.AccessModes._
import OpenOption._

import org.junit.Assert._
import org.junit.{
  Test, Ignore
}

import scalaio.test._

import java.io.{
    IOException, DataInputStream, DataOutputStream
}

abstract class FsOutputTests extends AbstractOutputTests with Fixture {

    final val DEFAULT_DATA = "to be overwritten"

    def open() : (Input, Output) = {
        val path = fixture.path
        (path, path)
    }

    @Test
    def create_file_with_write() : Unit = {
        val path = fixture.path
        val concretePath = path

        concretePath write DEFAULT_DATA.getBytes

        assertTrue(path.exists)

        val bytes = DEFAULT_DATA.getBytes
        assertEquals(Some(bytes.size), path.size)
        assertArrayEquals(bytes, concretePath.byteArray)
    }

    @Test
    def allow_use_of_data_input_stream : Unit = {
        val path = fixture.path
        val concretePath = path
        
        concretePath.outputStream() foreach {o =>
                val data = new DataOutputStream(o)
                data.writeShort(1)
                data.writeDouble(3.3)
                data.writeBoolean(false)
            }
            
        concretePath.inputStream() foreach {i=>
                val data = new DataInputStream(i)
                assertEquals(1, data.readShort)
                assertEquals(3.3, data.readDouble, 0.0000001)
                assertEquals(false, data.readBoolean)
            }
    }
}