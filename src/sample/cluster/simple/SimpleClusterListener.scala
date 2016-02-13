package sample.cluster.simple

import akka.cluster.Cluster
import akka.cluster.ClusterEvent._
import akka.actor.ActorLogging
import akka.actor.Actor

class SimpleClusterListener extends Actor with ActorLogging {

  val cluster = Cluster(context.system)

  override def preStart(): Unit = {
    cluster.subscribe(self, initialStateMode = InitialStateAsEvents,
      classOf[MemberEvent], classOf[UnreachableMember])
  }
  override def postStop(): Unit = cluster.unsubscribe(self)

  def receive = {
    case MemberUp(member) =>
      println("Member is Up: "+ member.address)
    case UnreachableMember(member) =>
      println("Member detected as unreachable: "+ member)
    case MemberRemoved(member, previousStatus) =>
      println("Member is Removed: "+member.address+"   after " + previousStatus)
    case _: MemberEvent => // ignore
  }
}
