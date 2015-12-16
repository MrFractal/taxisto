
/**
 * Created by max on 06.02.14.
 * 
 * 
 * card's infor representation
 * some fields will be empty for non-secure operations
 * 
 */

@interface CardInfo : NSObject {
  long id;
  NSString * number;
  int expirationMonth;
  int expirationYear;
  NSString * cvv;
}

@property(nonatomic) long id;
@property(nonatomic, retain) NSString * number;
@property(nonatomic) int expirationMonth;
@property(nonatomic) int expirationYear;
@property(nonatomic, retain) NSString * cvv;
- (void) setId:(long)id;
- (void) setNumber:(NSString *)number;
- (void) setExpirationMonth:(int)expirationMonth;
- (void) setExpirationYear:(int)expirationYear;
- (void) setCvv:(NSString *)cvv;
@end
