# emcs-tfe-reference-data

Retrieve CN Code information, packaging types or wine operations from the CandE Oracle database

This microservice allows you to switch between using Oracle and the reference-data stub based on a [feature switch in application.conf](conf/application.conf#L127-L129).

## API endpoints

<details>
<summary>Retrieve CN Code information

**`POST`** /oracle/cn-code-information</summary>

Retrieve CN Code information for a given list of Product Codes and CN Codes

**Request Body**: [CnInformationRequest Model](app/uk/gov/hmrc/emcstfereferencedata/models/request/CnInformationRequest.scala)

**Example request body**:

```json
{
  "items": [
    {
      "productCode": "B000",
      "cnCode": "22030001"
    },
    {
      "productCode": "S500",
      "cnCode": "10000000"
    }
  ]
}
```

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Key:value pair of String:[CnCodeInformation Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/CnCodeInformation.scala)

**Example response body**:

```json
{
  "24029000": {
    "cnCodeDescription": "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
    "exciseProductCodeDescription": "Fine-cut tobacco for the rolling of cigarettes",
    "unitOfMeasureCode": 1
  }
}
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>

<details>
<summary>Retrieve particular packaging types

**`POST`** /oracle/packaging-types</summary>

Retrieve packaging type information for a list of packaging types

**Request Body**: JSON array of packaging types

**Example request body**:

```json
[
  "VP",
  "NE",
  "TO"
]
```

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Key:value pair of String:String

**Example response body**:

```json
{
  "NE": "Unpacked or unpackaged",
  "TO": "Tun",
  "VP": "Vacuum-packed"
}
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>

<details>
<summary>Retrieve all packaging types

**`GET`** /oracle/packaging-types</summary>

Retrieve all packaging type information. An optional `isCountable` boolean query parameter
can be provided to find `countable` packaging types.
The response is sorted by the description in ascending (A-Z) order.

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Key:value pair of String:String

**Example response body**:

```json
{
  "CR": "Crate",
  "FD": "Framed crate",
  "VA": "Vat"
}
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>

<details>
<summary>Retrieve all wine operations

**`GET`** /oracle/wine-operations</summary>

Retrieve all wine operations.

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Key:value pair of String:String

**Example response body**:

```json
{
  "0": "The product has undergone none of the following operations",
  "1": "The product has been enriched",
  "2": "The product has been acidified",
  "3": "The product has been de-acidified"
}
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>

<details>
<summary>Retrieve particular wine operations

**`POST`** /oracle/wine-operations</summary>

Retrieve wine operation information for a list of wine operations

**Request Body**: JSON array of wine operations

**Example request body**:

```json
[
  "4",
  "11",
  "9"
]
```

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Key:value pair of String:String

**Example response body**:

```json
{
  "4": "The product has been sweetened",
  "11": "The product has been partially dealcoholised",
  "9": "The product has been made using oak chips"
}
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>

<details>
<summary>Retrieve member states

**`GET`** /oracle/member-states</summary>

Retrieve member states list

### Responses

#### Success Response(s)

**Status**: 200 (OK)

**Body**: Array of [Country](app/uk/gov/hmrc/emcstfereferencedata/models/response/Country.scala) object

**Example response body**:

```json
[
  {
    "countryCode": "FR",
    "country": "France"
  },
  {
    "countryCode": "AT",
    "country": "Austria"
  }
]
```

#### Error Response(s)

**Status**: 500 (ISE)

**Body**: [ErrorResponse Model](app/uk/gov/hmrc/emcstfereferencedata/models/response/ErrorResponse.scala)

</details>