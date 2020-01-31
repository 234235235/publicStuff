import java.io.{File => JFile}
import java.io.PrintWriter
import io.shiftleft.codepropertygraph.generated.nodes

def checkForChecks(strt: Int,checkList: List[nodes.TrackingPoint],debug: Boolean): String = {
	var res = ""
	
	var checks = List[String]("sizeof") 
	var comps = List[String](">","<","==","<=",">=")
	
	
	var indexes = (strt to checkList.length-1).toList
	indexes = indexes.reverse
	
	
	if (debug){
		res += "Called at Index: "+strt+"\n"
		res += "and List: \n"

		for (ix <- indexes){
			var ele = checkList.apply(ix)
			res += ele.location.lineNumber.head+": "+getCode(ele)+", "
		}
		res += "\n"
	}
	
	var vuln = true
	
	for (ix <- indexes){ //iterate through path
	
		var ele = checkList.apply(ix)
		var cde = getCode(ele)
		
		for (chk <- checks){ //iterate trough all possiblities to check (e.g. sizeof)
		
			if (cde.contains(chk)){  //if line containt a check (e.g. size = sizeof)
			
				if (debug){	res += "CHECK: "+ele.location.lineNumber.head+": "+cde+"\n" }
				
				var idxs = indexes.slice(indexes.indexOf(ix),indexes.length-1)
				
				for (idx <- idxs){ //check if this is also acutally used to compare (e.g. size > 1024)
				
					if (debug){ res += "--> "+getCode(checkList.apply(idx))+"\n"}
					
					var comp_ele = checkList.apply(idx)
					var comp_cde = getCode(comp_ele)
					
					for (comp <- comps){
						if (comp_cde.contains(comp)){
						
							if (debug){ res += "COMPARED: "+comp_ele.location.lineNumber.head+": "+comp_cde+"\n"}
							
							//TODO: check if its right variable!!
							
							vuln = false
						}
					}
				}
					
			}
		}
			
	}
	
	if (vuln){
		res += "\n[+] Vulnerable flow found!\n"
		for (it <- indexes){
			res += checkList.apply(it).location.lineNumber.head+": "+getCode(checkList.apply(it))+"\n"
		}
		res += "\n"
	
	}
	
	return res

}


def getCode(i: nodes.TrackingPoint): String = {
	if (i.isInstanceOf[Call]){
		return i.asInstanceOf[Call].code
	}
	else if(i.isInstanceOf[MethodParameterIn]){
		return i.asInstanceOf[MethodParameterIn].code
	}
	else{
		return "Not Supported: "+i.getClass
	}
	
	
}

def checkIfVuln(debug: Boolean) : String = {
	//val functionName = "not_vulnerable"
	
	val functionName = "vulnerable"
	
	var res = ""
	val src = cpg.method.name(functionName).parameter.name("s")
	val snk_candidate = cpg.method.name(functionName).callOut.name("strcpy")
	
	res += "[+] Checking for candidates:\n"
	res += snk_candidate.reachableByFlows(src).p.toString +"\n\n"
	
	
	
	//got candidates
	if (snk_candidate.reachableByFlows(src).l.length > 0){
		val snk = cpg.method.name(functionName).callOut
		res += "[+] Checking for constraints...\n"
		res += snk.reachableByFlows(src).p.toString+"\n\n"	
		
		var list = List[nodes.TrackingPoint]()		
		
		for (item <- snk.reachableByFlows(src).l){
			for (i <- item){
				if (!list.contains(i)){					
					list = i :: list
				}
			}			
			
		}
		
		list = list.sortWith((x,y) => x.location.lineNumber.head < y.location.lineNumber.head)
		
		if (debug){
			for (it <- list){
				res += it.location.lineNumber.head+": "+getCode(it)+"\n"
			}
		}
		
		var checkList = list.reverse
		
		
		
		var vulnFlows = ""
		
		val flw = snk_candidate.reachableByFlows(src).l
		for (hit <- flw){
			val lnNbr = hit.last.location.lineNumber.head
			
			for (ix <- 0 to checkList.length -1){
				if (checkList.apply(ix).location.lineNumber.head == lnNbr){
					vulnFlows += checkForChecks(ix,checkList,debug)
				}					
			}
		}
		
		
		res += vulnFlows
		res += "\n\n"
		
	}
	
	
	
	return res
	
}


@main def main(cpgFile: String="None",outFile: String="None"): Unit = {
	
	
	if (cpgFile!="None"){
		loadCpg(cpgFile);
	}

	
	var res = ""
	
	
	res += checkIfVuln(false)
	
	
	
	if (outFile != "None"){	
		val writer = new PrintWriter(new JFile(outFile));
		writer.write(res.toString());
		writer.close();
	}
	
	
	return 
}