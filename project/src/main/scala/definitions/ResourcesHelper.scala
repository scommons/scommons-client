package definitions

import java.io.{File, FileInputStream, FilterInputStream, InputStream}
import java.util.zip.ZipInputStream

import sbt._

object ResourcesHelper {

  def extractFromClasspath(targetDir: File,
                           cp: Def.Classpath,
                           fileFilter: FileFilter,
                           jarArtifactIds: Seq[String] = Nil): Unit = {

    for (cpEntry <- Attributed.data(cp) if cpEntry.exists) {
      val cpName = cpEntry.getName
      if (cpEntry.isFile && cpName.endsWith(".jar")) {
        if (jarArtifactIds.exists(cpName.contains)) {
          processEntries(new FileInputStream(cpEntry),
            pathName => fileFilter.accept(new File(pathName)),
            processEntry = { (relPath, stream) =>
              val targetFile = new File(targetDir, relPath)
              if (cpEntry.lastModified() != targetFile.lastModified()) {
                println(s"Copying file $targetFile")

                IO.write(targetFile, IO.readBytes(stream))
                targetFile.setLastModified(cpEntry.lastModified())
              }
            }
          )
        }
      } else if (cpEntry.isDirectory) {
        for ((file, relPath) <- Path.selectSubpaths(cpEntry, fileFilter)) {
          val targetFile = new File(targetDir, relPath)
          if (file.lastModified() != targetFile.lastModified()) {
            println(s"Copying file $targetFile")

            IO.copyFile(file, targetFile, preserveLastModified = true)
          }
        }
      } else {
        throw new IllegalArgumentException(
          "Illegal classpath entry, require directory or .jar file, but got: " + cpEntry.getPath)
      }
    }
  }

  private def processEntries(inputStream: InputStream,
                             p: String => Boolean,
                             processEntry: (String, InputStream) => Unit): Unit = {

    val stream = new ZipInputStream(inputStream)
    try {
      val streamIgnoreClose = new FilterInputStream(stream) {
        override def close(): Unit = ()
      }

      Iterator.continually(stream.getNextEntry)
        .takeWhile(_ != null)
        .filter(entry => p(entry.getName))
        .foreach(entry => processEntry(entry.getName, streamIgnoreClose))
    } finally {
      stream.close()
    }
  }
}
