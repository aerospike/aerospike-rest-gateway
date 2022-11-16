# Rest Gateway Expressions

The Aerospike REST gateway 1.7.0 and newer supports filter expressions added in server 5.6. The now deprecated predicate
expressions are supported in REST gateway 1.11.0 and older.

**Important:** The DSL used to define predicate expressions can also be used to define filter expressions.

There are two ways to define a filter expression and provide it as part of a URL query parameter. For more information
on which APIs
support filter expression take a look at
the [Swagger UI API documentation] [Aerospike REST Gateway API Documentation](https://docs.aerospike.com/apidocs/rest).
The first way to define a filter expression, is to
use the DSL. The second, is to use one of our supporting client libraries to define an expression and serialize it to a
Base64 encoded string using the provided APIs for that language.

For a quick overview of the DSL please take a look at our "Dealing with Predicate Expression Filters" blog post series.

1. [Dealing with Predicate Expression Filters in Aerospike REST Gateway (Part 1)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-1-a43e43ac8c7d?source=friends_link&sk=bc0ed64110578ff6f4804753ca6369da)
2. [Dealing with Predicate Expression Filters in Aerospike REST Gateway (Part 2)](https://medium.com/aerospike-developer-blog/dealing-with-predicate-expression-filters-in-aerospike-rest-client-part-2-b9d9358c8a4e?source=friends_link&sk=35c37b035d12789aae6272704ef95829)

The second method requires that we write a small amount of code to build an Expression object and serialize it to a
base64 encoded string. Currently, the only languages that support this feature are C, C#, and Java with more to come.
You can
either setup a small project in your favorite IDE or you can use Aerospike's
coding [sandbox](https://developer.aerospike.com/tutorials/sandbox).

For example, the following creates an expression using
the [Java Client API](https://javadoc.io/doc/com.aerospike/aerospike-client/6.1.2/index.html) that returns `true`
or `false` if the product of `intBin1` and `intBin2` is greater than `15`.
You can copy and paste the following code snippet into the [sandbox](https://developer.aerospike.com/tutorials/sandbox)
to try it out for yourself.

**Note:** The sandbox does not require, nor does it allow, you to define an entry class.

```java
import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;

Expression exp=Exp.build(
    Exp.gt(
        Exp.mul(
            Exp.intBin("intBin1"),
            Exp.intBin("intBin2")
        ),
    Exp.val(15L)
    )
);
System.out.println(exp.getBase64());
```

which will print out your base64 encodes expression `kwOTFpNRAqdpbnRCaW4xk1ECp2ludEJpbjIP`. The base64 encoded string
can now be provided as a `filter_expression` query parameter.