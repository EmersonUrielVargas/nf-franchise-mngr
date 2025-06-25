resource "aws_lb" "franchise_alb" {
  name               = "franchise-alb"
  internal           = false
  load_balancer_type = "application"
  subnets            = var.service_subnet_ids
  security_groups    = [var.service_security_group_id]
}

resource "aws_lb_target_group" "franchise_alb_tg" {
  name     = "franchise-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = var.alb_vpc_id
  target_type = "ip"
  health_check {
    path = "/"
    protocol = "HTTP"
  }
}

resource "aws_lb_listener" "franchise_alb_listener" {
  load_balancer_arn = aws_lb.franchise_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.franchise_alb_tg.arn
  }
}