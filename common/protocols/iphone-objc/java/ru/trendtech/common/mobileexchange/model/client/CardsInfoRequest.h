
@interface CardsInfoRequest : NSObject {
  long clientId;
  CardInfo * cardInfo;
}

@property(nonatomic) long clientId;
@property(nonatomic, retain) CardInfo * cardInfo;
- (void) setClientId:(long)clientId;
- (void) setCardInfo:(CardInfo *)cardInfo;
@end
