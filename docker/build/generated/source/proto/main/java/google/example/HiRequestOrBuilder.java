// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: google/example/hello.proto

package google.example;

public interface HiRequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:google.example.HiRequest)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string query = 1;</code>
   */
  java.lang.String getQuery();
  /**
   * <code>string query = 1;</code>
   */
  com.google.protobuf.ByteString
      getQueryBytes();

  /**
   * <pre>
   * extra properties to demonstrate how the code generator
   * works for repeated and map field types
   * </pre>
   *
   * <code>repeated string tags = 2;</code>
   */
  java.util.List<java.lang.String>
      getTagsList();
  /**
   * <pre>
   * extra properties to demonstrate how the code generator
   * works for repeated and map field types
   * </pre>
   *
   * <code>repeated string tags = 2;</code>
   */
  int getTagsCount();
  /**
   * <pre>
   * extra properties to demonstrate how the code generator
   * works for repeated and map field types
   * </pre>
   *
   * <code>repeated string tags = 2;</code>
   */
  java.lang.String getTags(int index);
  /**
   * <pre>
   * extra properties to demonstrate how the code generator
   * works for repeated and map field types
   * </pre>
   *
   * <code>repeated string tags = 2;</code>
   */
  com.google.protobuf.ByteString
      getTagsBytes(int index);

  /**
   * <code>map&lt;string, string&gt; flags = 3;</code>
   */
  int getFlagsCount();
  /**
   * <code>map&lt;string, string&gt; flags = 3;</code>
   */
  boolean containsFlags(
      java.lang.String key);
  /**
   * Use {@link #getFlagsMap()} instead.
   */
  @java.lang.Deprecated
  java.util.Map<java.lang.String, java.lang.String>
  getFlags();
  /**
   * <code>map&lt;string, string&gt; flags = 3;</code>
   */
  java.util.Map<java.lang.String, java.lang.String>
  getFlagsMap();
  /**
   * <code>map&lt;string, string&gt; flags = 3;</code>
   */

  java.lang.String getFlagsOrDefault(
      java.lang.String key,
      java.lang.String defaultValue);
  /**
   * <code>map&lt;string, string&gt; flags = 3;</code>
   */

  java.lang.String getFlagsOrThrow(
      java.lang.String key);
}
