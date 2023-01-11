/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.services

import uk.gov.hmrc.emcstfereferencedata.config.AppConfig

import java.util.Date
import javax.inject.{Inject, Singleton}
import uk.gov.hmrc.emcstfereferencedata.models.{ConnectivityResult, DBOptions, DataCentreConnectivityResult, EMCSDbTestResult}

trait DBConnectivityResultCache{
  def all: EMCSDbTestResult
  def put(c: Map[DBOptions, ConnectivityResult])
}

@Singleton
class MemoryDBConnectivityResultCacheService @Inject()(val config: AppConfig) extends DBConnectivityResultCache {

  var currentResults : Map[DBOptions, ConnectivityResult] = Map()
  var lastResultDate = new Date()
  var resultsStateLastChangedDate = new Date()

  def put(newResults: Map[DBOptions, ConnectivityResult]) = {
    val resultStateChanged : Boolean = currentResults.isEmpty || currentResults.forall(_._2.connectionOK) != newResults.forall(_._2.connectionOK)

    currentResults = newResults
    lastResultDate = new Date()
    if(resultStateChanged) resultsStateLastChangedDate = lastResultDate
  }

  def all: EMCSDbTestResult = {
    createResult(currentResults.values)
  }

  def createResult(input: Iterable[ConnectivityResult]) : EMCSDbTestResult  = {

    val dbResults = input.toSeq
      .sortBy(_.name)
      .groupBy(_.dataCentre)
      .map(a => DataCentreConnectivityResult(a._1, a._2.forall(_.connectionOK), a._2))
      .toSeq

    EMCSDbTestResult(dbResults, lastResultDate, resultsStateLastChangedDate)
  }

}