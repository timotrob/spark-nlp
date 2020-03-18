package com.johnsnowlabs.nlp.embeddings

import java.io.File

import com.johnsnowlabs.ml.tensorflow.TensorflowSentencePiece
import com.johnsnowlabs.ml.tensorflow.TensorflowWrapper
import com.johnsnowlabs.nlp.{Annotation, SparkNLP}
import com.johnsnowlabs.nlp.annotators.Tokenizer
import com.johnsnowlabs.nlp.annotators.sbd.pragmatic.SentenceDetector
import com.johnsnowlabs.nlp.base.DocumentAssembler
import com.johnsnowlabs.nlp.util.io.ResourceHelper.spark.implicits._
import org.apache.spark.ml.Pipeline
import org.apache.spark.sql.functions.{explode, size}
import org.scalatest._

/** *
  * Sentence Piece Model is used to Tensorflow Weights from a tf_sentencepiece model.
  * A Sentence Piece Model generates a set of tokens and ids for input text
  */
class SentencePieceTokenizationTestSpec extends FlatSpec {
  "Sentence Piece Embeddings" should "generate Tokens and ID's" in {
    val dictPath = "/home/loan/Documents/JohnSnowLabs/Docs/PR/src/test/scala/com/johnsnowlabs/nlp/embeddings/768_xlnet_dict.txt"
    val spModelPath = "/home/loan/Documents/JohnSnowLabs/XLNet/jupyter/SentencePiece/exported_model"
    val soOperationsPath = "/home/loan/venv/XLNET_jupyter_venv/lib/python2.7/site-packages/tf_sentencepiece/_sentencepiece_processor_ops.so.1.14.0"
    val sentencePieceTokenizer = SentencePieceTokens.loadSavedModel(spModelPath, soOperationsPath, dictPath, SparkNLP.start()) //Create Spiece Model

    val sampleSentence = "Everything that has a beginning has an end."
    val annot = Annotation(annotatorType = "-1", begin = -1, end = -1,
      result = sampleSentence,
      metadata = Map[String, String](), embeddings = Array[Float]())

    val ids = sentencePieceTokenizer.encode(Seq(annot))


    println("Input sentence" + sampleSentence)
    println("Correct ids      : 8248,29,51,24,1278,51,48,239,9")
    println("Resulting ids  : " + ids(0).tokens.map(_.pieceId).map(_.toString).mkString(","))


    val decodeIds = Seq(Seq(8248, 29, 51, 24, 1278, 51, 48, 239, 9))
    val decodedTokens = sentencePieceTokenizer.decode(decodeIds)
    println("Decoded String " + decodedTokens(0).tokens.map(_.token).foldLeft("")(_ + _))

    println("From IDs " + decodeIds)
    println("Decoded String " + decodedTokens(0).tokens.map(_.pieceId).map(_.toString).mkString(","))


  }


}
