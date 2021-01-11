function toggleResetPswd(e){
    e.preventDefault();
    $('#logreg-forms .form-signin').toggle() // display:block or none
    $('#logreg-forms .form-reset').toggle() // display:block or none
}

function toggleSignUp(e){
    e.preventDefault();
    $('#logreg-forms .form-signin').toggle(); // display:block or none
    $('#logreg-forms .form-signup').toggle(); // display:block or none
}

$(()=>{
    // Login Register Form
    $('#logreg-forms #forgot_pswd').click(toggleResetPswd);
    $('#logreg-forms #cancel_reset').click(toggleResetPswd);
    $('#logreg-forms #btn-signup').click(toggleSignUp);
    $('#logreg-forms #cancel_signup').click(toggleSignUp);
})



/**
 * 
 */

$(document).ready(function() {
	$('.delete-product').on('click', function (){
		/*<![CDATA[*/
	    var path = /*[[@{/}]]*/'remove';
	    /*]]>*/
		
		var id=$(this).attr('id');
		
		bootbox.confirm({
			message: "Are you sure to remove this book? It can't be undone.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.post(path, {'id':id}, function(res) {
						location.reload();
					});
				}
			}
		});
	});
	
	
	
//	$('.checkboxBook').click(function () {
//        var id = $(this).attr('id');
//        if(this.checked){
//            bookIdList.push(id);
//        }
//        else {
//            bookIdList.splice(bookIdList.indexOf(id), 1);
//        }
//    })
	
	$('#deleteSelected').click(function() {
		var idList= $('.checkboxProduct');
		var productIdlist=[];
		for (var i = 0; i < idList.length; i++) {
			if(idList[i].checked==true) {
				productIdlist.push(idList[i]['id'])
			}
		}
		
		console.log(productIdlist);
		
		/*<![CDATA[*/
	    var path = /*[[@{/}]]*/'removeList';
	    /*]]>*/
	    
	    bootbox.confirm({
			message: "Are you sure to remove all selected books? It can't be undone.",
			buttons: {
				cancel: {
					label:'<i class="fa fa-times"></i> Cancel'
				},
				confirm: {
					label:'<i class="fa fa-check"></i> Confirm'
				}
			},
			callback: function(confirmed) {
				if(confirmed) {
					$.ajax({
						type: 'POST',
						url: path,
						data: JSON.stringify(productIdlist),
						contentType: "application/json",
						success: function(res) {
							console.log(res); 
							location.reload()
							},
						error: function(res){
							console.log(res); 
							location.reload();
							}
					});
				}
			}
		});
	});
	
	$("#selectAllProduct").click(function() {
		if($(this).prop("checked")==true) {
			$(".checkboxProduct").prop("checked",true);
		} else if ($(this).prop("checked")==false) {
			$(".checkboxProduct").prop("checked",false);
		}
	})
});

$(document).ready(function() {
    $(".dropdown-toggle").dropdown();
})

/**
 * 
 */

$(document).ready(function() {
	$('#fileImage').change(function(){
		showImageThumbnail(this);
	});
});

function showImageThumbnail(fileInput)
{
	file=fileInput.files[0];
	reader=new FileReader();
	
	
	reader.onload=function(e){
		$('#thumbnail').attr('src',e.target.result)
	};
	
	reader.readAsDataURL(file);
}

function random()
{
	// alert('Working')
	var a=document.getElementById('gender').value;
	
	if(a==='Men')
		{
		var array=["T-Shirts","Shirts","Pants","Jeans","Suits","Inner Wear"];
		}
	
	else if(a==='Women')
	{
	var array=["Indian Wear","Western Wear","Sleep Wear and Lingerie","Saree"];
	}
	
	else if(a==='Kids')
	{
	var array=["Boys Wear","Girls Wear"];
	}
	else
		{
		var array=[];
		}
	
	var string=" ";
	
	for(i=0;i<array.length;i++)
		{
		string=string+"<option>"+array[i]+"</option>";
		}
	
	string="<select name='category'>"+string+"</select>";
	
	document.getElementById('category').innerHTML=string;
}

function getPrice()
{
	
	var num1=Number(document.getElementById("productPrice").value);
	var num2=Number(document.getElementById("productdiscount").value)/100;
	var num3=num1-(num1*num2);
	document.getElementById("productPriceAfterdiscount").value=num3.toFixed(2);
}


/**
 * 
 */

function checkBillingAddress() {
	if($("#theSameAsShippingAddress").is(":checked")) {
		$(".billingAddress").prop("disabled", true);
	} else {
		$(".billingAddress").prop("disabled", false);
	}
}

function checkPasswordMatch() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();
	
	if(password == "" && confirmPassword =="") {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if(password != confirmPassword) {
			$("#checkPasswordMatch").html("Passwords do not match!");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {
			$("#checkPasswordMatch").html("Passwords match");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
}


$(document).ready(function(){
	$(".cardItemQty").on('change',function(){
		
		var id=this.id;
		console.log("test");
		$('#update-item-'+id).css('display','inline-block');
	});
	$("#theSameAsShippingAddress").on('click',checkBillingAddress);
	$("#txtConfirmPassword").keyup(checkPasswordMatch);
	$("#txtNewPassword").keyup(checkPasswordMatch);
});


$(document).ready(function(){
	  $('.dropdown-submenu a.test').on("click", function(e){
	    $(this).next('ul').toggle();
	    e.stopPropagation();
	    e.preventDefault();
	  });
	});