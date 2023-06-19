export function formattime(obj) {
  var result;

  let today = new Date();
  // let uploadTime = new Date(year, month - 1, day, hour, min, sec);
  let uploadTime = new Date(obj);

  var diff = Math.floor((today - uploadTime) / 1000);

  var yearToSecond = 12 * 30 * 24 * 60 * 60;
  var monthToSecond = 30 * 24 * 60 * 60;
  var dayToSecond = 24 * 60 * 60;
  var hourToSecond = 60 * 60;
  var minuteToSecond = 60;

  if (diff / yearToSecond >= 1) {
    result = Math.floor(diff / yearToSecond).toString() + "년전";
  } else if (diff / monthToSecond >= 1) {
    result = Math.floor(diff / monthToSecond).toString() + "개월전";
  } else if (diff / dayToSecond >= 1) {
    result = Math.floor(diff / dayToSecond).toString() + "일전";
  } else if (diff / hourToSecond >= 1) {
    result = Math.floor(diff / hourToSecond).toString() + "시간전";
  } else if (diff / minuteToSecond >= 1) {
    result = Math.floor(diff / minuteToSecond).toString() + "분전";
  } else {
    result = diff.toString() + "초전";
  }

  return result;
}

export function formatViews(views) {
  var result = "";
  views = parseFloat(views);

  if (Math.floor(views / 10000) > 0) {
    result = (Math.floor((views / 10000) * 10) / 10).toString() + "만회";
  } else if (Math.floor(views / 1000) > 0) {
    result = (Math.floor((views / 1000) * 10) / 10).toString() + "천회";
  } else {
    result = views.toString() + "회";
  }
  return result;
}

export function getVideoState(obj) {
  var result;
  if (obj == "public") {
    result = "공개";
  } else if (obj == "private") {
    result = "비공개";
  }
  return result;
}
